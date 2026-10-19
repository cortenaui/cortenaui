/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.primitives

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCoerceAtLeast
import androidx.compose.ui.util.fastCoerceIn
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxOfOrNull
import framework.cortena.ui.annotation.ExperimentalComponentsApi
import framework.cortena.ui.interaction.DampedAnimation
import framework.cortena.ui.interaction.applyInteractiveAnimation

/**
 * A track with a thumb that slides along it, driven by a [DampedAnimation]. The shared base of
 * `Slider` and `Toggle`.
 *
 * The thumb sits at the start when [DampedAnimation.progress] is `0` and at the end when it is `1`,
 * mirrored in right-to-left layouts. It gets the press motion from [applyInteractiveAnimation] with
 * the animation's press scale. Both slots are centered vertically, and the control is as wide and
 * as tall as its largest slot.
 *
 * `DragTrack` detects no gestures. Attach them through [modifier] or inside [track], and drive the
 * animation with [DampedAnimation.updateValue], [DampedAnimation.press], and
 * [DampedAnimation.release], or attach [DampedAnimation.modifier].
 *
 * @param animation Value and press physics for the thumb.
 * @param modifier Applied to the whole control.
 * @param thumbPadding Gap between the thumb and each end of the track.
 * @param track Track content. Usually fills the control and draws from [DampedAnimation.progress].
 * @param thumb Thumb content. It moves and scales as one layer, shadow included.
 */
@ExperimentalComponentsApi
@Composable
fun DragTrack(
    animation: DampedAnimation,
    modifier: Modifier = Modifier,
    thumbPadding: Dp = 0.dp,
    track: @Composable () -> Unit,
    thumb: @Composable () -> Unit,
) {
    Layout(contents = listOf(track, thumb), modifier = modifier) {
        (trackMeasurables, thumbMeasurables),
        constraints ->
        val childConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val trackPlaceables = trackMeasurables.fastMap { it.measure(childConstraints) }
        val thumbPlaceables = thumbMeasurables.fastMap { it.measure(childConstraints) }
        val width =
            constraints.constrainWidth(
                maxOf(
                    trackPlaceables.fastMaxOfOrNull { it.width } ?: 0,
                    thumbPlaceables.fastMaxOfOrNull { it.width } ?: 0,
                )
            )
        val height =
            constraints.constrainHeight(
                maxOf(
                    trackPlaceables.fastMaxOfOrNull { it.height } ?: 0,
                    thumbPlaceables.fastMaxOfOrNull { it.height } ?: 0,
                )
            )
        val padding = thumbPadding.toPx()
        val isLtr = layoutDirection == LayoutDirection.Ltr

        layout(width, height) {
            trackPlaceables.fastForEach {
                it.place(0, Alignment.CenterVertically.align(it.height, height))
            }
            thumbPlaceables.fastForEach { placeable ->
                placeable.placeWithLayer(
                    0,
                    Alignment.CenterVertically.align(placeable.height, height),
                ) {
                    // Read in the layer block, so a moving thumb redraws without a relayout
                    val travel = (width - placeable.width - padding * 2f).fastCoerceAtLeast(0f)
                    val progress = animation.progress.fastCoerceIn(0f, 1f)
                    applyInteractiveAnimation(
                        pressProgress = animation.pressProgress,
                        offset = Offset.Zero,
                        baseTranslationX =
                            padding + travel * if (isLtr) progress else 1f - progress,
                        scaleXMultiplier = animation.scaleX,
                        scaleYMultiplier = animation.scaleY,
                    )
                }
            }
        }
    }
}
