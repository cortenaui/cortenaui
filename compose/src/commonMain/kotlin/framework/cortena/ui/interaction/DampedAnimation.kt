/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.interaction

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.IntSize
import kotlin.math.abs
import kotlin.time.TimeSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Spring physics for a draggable value, such as a slider thumb or a toggle knob.
 *
 * Runs independent spring tracks for the value, its velocity, the press progress, and the
 * horizontal and vertical press scale. Read them inside `graphicsLayer` or a draw block so each
 * frame skips recomposition. Create it in `remember(animationScope)` with a scope from
 * `rememberCoroutineScope()`.
 *
 * @param animationScope Scope that runs the animations. Its context needs a frame clock, which
 *   `rememberCoroutineScope()` provides.
 * @param initialValue Starting value, inside [valueRange].
 * @param valueRange Range that [updateValue] clamps to and that [progress] maps to `0..1`.
 * @param visibilityThreshold Smallest value change worth animating.
 * @param initialScale Scale at rest.
 * @param pressedScale Scale while pressed.
 * @param onDragStarted Called by [modifier] when a press starts, with the touch position.
 * @param onDragStopped Called by [modifier] when the press ends or is canceled.
 * @param onDrag Called by [modifier] for every move, with the size of the element that carries
 *   [modifier] and the move delta in pixels.
 */
class DampedAnimation(
    private val animationScope: CoroutineScope,
    val initialValue: Float,
    val valueRange: ClosedRange<Float>,
    val visibilityThreshold: Float,
    val initialScale: Float,
    val pressedScale: Float,
    val onDragStarted: DampedAnimation.(position: Offset) -> Unit,
    val onDragStopped: DampedAnimation.() -> Unit,
    val onDrag: DampedAnimation.(size: IntSize, dragAmount: Offset) -> Unit,
) {

    // Multi-track spring specs. These deliberately do NOT pull from LocalMotion: each track
    // (position, velocity, press progress, scale-x, scale-y) needs its own dampingRatio /
    // stiffness / visibilityThreshold tuple to feel right under continuous gesture input.
    // The standard three-tier presets in :motion are for content-level animations; raw gesture
    // physics is the one place where bespoke specs are correct. Touch with care.
    private val valueAnimationSpec = spring(1f, 1000f, visibilityThreshold)
    private val velocityAnimationSpec = spring(0.5f, 300f, visibilityThreshold * 10f)
    private val pressProgressAnimationSpec = spring(1f, 1000f, 0.001f)
    private val scaleXAnimationSpec = spring(0.6f, 250f, 0.001f)
    private val scaleYAnimationSpec = spring(0.7f, 250f, 0.001f)

    private val valueAnimation = Animatable(initialValue, visibilityThreshold)
    private val velocityAnimation = Animatable(0f, 5f)
    private val pressProgressAnimation = Animatable(0f, 0.001f)
    private val scaleXAnimation = Animatable(initialScale, 0.001f)
    private val scaleYAnimation = Animatable(initialScale, 0.001f)

    private val velocityTracker = VelocityTracker()
    private val startMark = TimeSource.Monotonic.markNow()

    /** Current animated value. */
    val value: Float
        get() = valueAnimation.value

    /** [value] as a fraction of [valueRange]: `0` at the start, `1` at the end. */
    val progress: Float
        get() = (value - valueRange.start) / (valueRange.endInclusive - valueRange.start)

    /** Value the spring is heading to, set by [updateValue]. */
    val targetValue: Float
        get() = valueAnimation.targetValue

    /** `0` at rest, `1` while pressed. */
    val pressProgress: Float
        get() = pressProgressAnimation.value

    /** Horizontal press scale, between [initialScale] and [pressedScale]. */
    val scaleX: Float
        get() = scaleXAnimation.value

    /** Vertical press scale, between [initialScale] and [pressedScale]. */
    val scaleY: Float
        get() = scaleYAnimation.value

    /** Smoothed speed of [value] in range widths per second. Positive while the value grows. */
    val velocity: Float
        get() = velocityAnimation.value

    /**
     * Pointer input that calls [onDragStarted], [onDrag], and [onDragStopped], and runs [press] and
     * [release]. Components that detect gestures themselves call [press] and [release] directly
     * instead.
     */
    val modifier: Modifier =
        Modifier.pointerInput(Unit) {
            inspectDragGestures(
                onDragStart = { down ->
                    onDragStarted(down.position)
                    press()
                },
                onDragEnd = {
                    onDragStopped()
                    release()
                },
                onDragCancel = {
                    onDragStopped()
                    release()
                },
            ) { _, dragAmount ->
                onDrag(size, dragAmount)
            }
        }

    /** Animates [pressProgress] to `1` and both scales to [pressedScale]. */
    fun press() {
        velocityTracker.resetTracking()
        animationScope.launch {
            launch { pressProgressAnimation.animateTo(1f, pressProgressAnimationSpec) }
            launch { scaleXAnimation.animateTo(pressedScale, scaleXAnimationSpec) }
            launch { scaleYAnimation.animateTo(pressedScale, scaleYAnimationSpec) }
        }
    }

    /**
     * Animates [pressProgress] to `0` and both scales to [initialScale]. While [value] is still
     * moving, it first waits until the value is within 2.5% of the range from [targetValue].
     */
    fun release() {
        animationScope.launch {
            withFrameNanos {}
            if (value != targetValue) {
                val threshold = (valueRange.endInclusive - valueRange.start) * 0.025f
                snapshotFlow { valueAnimation.value }
                    .filter { abs(it - valueAnimation.targetValue) < threshold }
                    .first()
            }
            launch { pressProgressAnimation.animateTo(0f, pressProgressAnimationSpec) }
            launch { scaleXAnimation.animateTo(initialScale, scaleXAnimationSpec) }
            launch { scaleYAnimation.animateTo(initialScale, scaleYAnimationSpec) }
        }
    }

    /** Springs toward [value], clamped to [valueRange], and updates [velocity] on the way. */
    fun updateValue(value: Float) {
        val targetValue = value.coerceIn(valueRange)
        animationScope.launch {
            launch {
                valueAnimation.animateTo(targetValue, valueAnimationSpec) { updateVelocity() }
            }
        }
    }

    private fun updateVelocity() {
        velocityTracker.addPosition(
            startMark.elapsedNow().inWholeMilliseconds,
            Offset(value, 0f),
        )
        val targetVelocity =
            velocityTracker.calculateVelocity().x / (valueRange.endInclusive - valueRange.start)
        animationScope.launch { velocityAnimation.animateTo(targetVelocity, velocityAnimationSpec) }
    }
}
