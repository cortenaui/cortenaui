/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCoerceIn
import androidx.compose.ui.util.lerp
import framework.cortena.ui.annotation.ExperimentalComponentsApi
import framework.cortena.ui.color.ColorToken
import framework.cortena.ui.graphics.shadow.Shadow
import framework.cortena.ui.interaction.DampedAnimation
import framework.cortena.ui.interaction.inspectDragGestures
import framework.cortena.ui.primitives.Container
import framework.cortena.ui.primitives.DragTrack
import framework.cortena.ui.shape.CapsuleShape
import framework.cortena.ui.size.SizeToken
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.theme.LocalSizeToken
import framework.cortena.ui.theme.LocalSpacing
import framework.cortena.ui.theme.sliderIndicatorWidth
import framework.cortena.ui.theme.sliderTrackHeight
import framework.cortena.ui.theme.value
import kotlin.math.max
import kotlin.math.round
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalComponentsApi::class)
@Composable
fun Slider(
    value: () -> Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    steps: Int = 0,
    size: SizeToken = LocalSizeToken.current,
    indicatorColor: Color = Color.Unspecified,
    trackColor: Color = Color.Unspecified,
    progressColor: Color = Color.Unspecified,
) {
    val colors = LocalColors.current
    val spacing = LocalSpacing.current

    val shape = CapsuleShape()
    val indicatorWidth = size.sliderIndicatorWidth
    val indicatorHeight = size.sliderTrackHeight + spacing.Sm.dp
    val trackHeight = size.sliderTrackHeight
    val resolvedTrackColor =
        if (trackColor.isSpecified) trackColor else Color(colors.surfaceVariant)
    val resolvedIndicatorColor = if (indicatorColor.isSpecified) indicatorColor else Color.White
    val resolvedProgressColor =
        if (progressColor.isSpecified) progressColor else Color(colors.primary)
    val indicatorShadow = sliderIndicatorShadow(resolvedTrackColor, enabled)

    val layoutDirection = LocalLayoutDirection.current
    val isLtr = layoutDirection == LayoutDirection.Ltr
    val animationScope = rememberCoroutineScope()
    val dampedAnimation =
        remember(animationScope) {
            DampedAnimation(
                animationScope = animationScope,
                initialValue = value(),
                valueRange = valueRange,
                visibilityThreshold = 0.01f,
                initialScale = 1f,
                pressedScale = 1.3f,
                // Slider detects its own gestures below and never attaches the drag modifier
                onDragStarted = {},
                onDragStopped = {},
                onDrag = { _, _ -> },
            )
        }

    LaunchedEffect(dampedAnimation) {
        snapshotFlow { value() }
            .collectLatest { targetValue ->
                if (dampedAnimation.targetValue != targetValue) {
                    dampedAnimation.updateValue(targetValue)
                }
            }
    }

    val gestureModifier =
        if (enabled) {
            Modifier.pointerInput(valueRange, layoutDirection, steps) {
                fun valueFromPosition(x: Float): Float {
                    if (this.size.width == 0) {
                        return value()
                    }
                    val positionProgress =
                        sliderProgressFromPosition(
                            x = x,
                            trackWidth = this.size.width.toFloat(),
                            indicatorWidth = indicatorWidth.toPx(),
                            isLtr = isLtr,
                        )
                    if (steps > 0) {
                        val intervalCount = steps + 1
                        val snappedProgress =
                            round(positionProgress * intervalCount) / intervalCount
                        return lerp(valueRange.start, valueRange.endInclusive, snappedProgress)
                    }
                    return lerp(valueRange.start, valueRange.endInclusive, positionProgress)
                }
                inspectDragGestures(
                    onDragStart = { down ->
                        val targetValue = valueFromPosition(down.position.x)
                        dampedAnimation.press()
                        onValueChange(targetValue)
                    },
                    onDragEnd = { dampedAnimation.release() },
                    onDragCancel = { dampedAnimation.release() },
                ) { change, _ ->
                    val targetValue = valueFromPosition(change.position.x)
                    onValueChange(targetValue)
                }
            }
        } else {
            Modifier
        }

    DragTrack(
        animation = dampedAnimation,
        modifier = modifier.fillMaxWidth(),
        track = {
            Box(
                Modifier.clip(shape)
                    .graphicsLayer { alpha = if (enabled) 1f else 0.38f }
                    .background(resolvedTrackColor)
                    .drawBehind {
                        val progress = dampedAnimation.progress.fastCoerceIn(0f, 1f)
                        val progressEdge =
                            sliderProgressEdge(
                                    trackWidth = this.size.width,
                                    indicatorWidth = indicatorWidth.toPx(),
                                    progress = progress,
                                    isLtr = isLtr,
                                )
                                .fastCoerceIn(0f, this.size.width)
                        drawRect(
                            resolvedProgressColor,
                            topLeft =
                                if (isLtr) {
                                    Offset.Zero
                                } else {
                                    Offset(progressEdge, 0f)
                                },
                            size =
                                this.size.copy(
                                    width =
                                        if (isLtr) {
                                            progressEdge
                                        } else {
                                            this.size.width - progressEdge
                                        }
                                ),
                        )
                        if (steps > 0) {
                            val intervalCount = steps + 1
                            val tickRadius = 4.dp.toPx()
                            for (i in 0..intervalCount) {
                                val tickProgress = i.toFloat() / intervalCount
                                val cx =
                                    sliderProgressEdge(
                                        trackWidth = this.size.width,
                                        indicatorWidth = indicatorWidth.toPx(),
                                        progress = tickProgress,
                                        isLtr = isLtr,
                                    )
                                val isTickActive = tickProgress <= progress
                                val tickColor =
                                    if (isTickActive) {
                                        // Covered by progress: lighten the progressColor
                                        Color(
                                            red =
                                                (resolvedProgressColor.red +
                                                    (1f - resolvedProgressColor.red) * 0.50f),
                                            green =
                                                (resolvedProgressColor.green +
                                                    (1f - resolvedProgressColor.green) * 0.50f),
                                            blue =
                                                (resolvedProgressColor.blue +
                                                    (1f - resolvedProgressColor.blue) * 0.50f),
                                            alpha = resolvedProgressColor.alpha,
                                        )
                                    } else {
                                        // Not covered: darken the trackColor
                                        Color(
                                            red = resolvedTrackColor.red * 0.50f,
                                            green = resolvedTrackColor.green * 0.50f,
                                            blue = resolvedTrackColor.blue * 0.50f,
                                            alpha = resolvedTrackColor.alpha,
                                        )
                                    }
                                drawCircle(
                                    color = tickColor,
                                    radius = tickRadius,
                                    center = Offset(cx, this.size.height / 2f),
                                )
                            }
                        }
                    }
                    .then(gestureModifier)
                    .fillMaxWidth()
            ) {
                Box(modifier = Modifier.height(trackHeight))
            }
        },
        thumb = {
            Container(
                modifier = Modifier.size(width = indicatorWidth, height = indicatorHeight),
                shape = shape,
                color =
                    if (!enabled) {
                        val isLight = resolvedTrackColor.luminance() > 0.5f
                        val dimFactor = if (isLight) 0.96f else 0.5f
                        Color(
                            red = resolvedIndicatorColor.red * dimFactor,
                            green = resolvedIndicatorColor.green * dimFactor,
                            blue = resolvedIndicatorColor.blue * dimFactor,
                            alpha = resolvedIndicatorColor.alpha,
                        )
                    } else {
                        resolvedIndicatorColor
                    },
                shadow = indicatorShadow,
            )
        },
    )
}

private fun sliderProgressFromPosition(
    x: Float,
    trackWidth: Float,
    indicatorWidth: Float,
    isLtr: Boolean,
): Float {
    val start = indicatorWidth * 0.5f
    val end = trackWidth - indicatorWidth * 0.5f
    val travel = end - start
    val progress = if (travel > 0f) ((x - start) / travel).fastCoerceIn(0f, 1f) else 0f
    return if (isLtr) progress else 1f - progress
}

private fun sliderIndicatorStart(
    trackWidth: Float,
    indicatorWidth: Float,
    progress: Float,
    isLtr: Boolean,
): Float {
    val availableWidth = max(0f, trackWidth - indicatorWidth)
    val directedProgress = if (isLtr) progress else 1f - progress
    return availableWidth * directedProgress.fastCoerceIn(0f, 1f)
}

private fun sliderProgressEdge(
    trackWidth: Float,
    indicatorWidth: Float,
    progress: Float,
    isLtr: Boolean,
): Float {
    return sliderIndicatorStart(
        trackWidth = trackWidth,
        indicatorWidth = indicatorWidth,
        progress = progress,
        isLtr = isLtr,
    ) + indicatorWidth * 0.5f
}

@Composable
private fun sliderIndicatorShadow(trackColor: Color, enabled: Boolean): Shadow {
    val isLight = trackColor.luminance() > 0.5f
    val alphaLight =
        if (enabled) {
            0.28f
        } else {
            0.15f
        }
    return Shadow(
        radius = if (isLight) 12.dp else 8.dp,
        offset = DpOffset(0.dp, if (isLight) 3.dp else 2.dp),
        color = ColorToken.Gray900.value().copy(alpha = if (isLight) alphaLight else 0.18f),
    )
}
