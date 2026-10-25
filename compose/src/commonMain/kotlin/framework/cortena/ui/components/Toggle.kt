/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCoerceIn
import framework.cortena.ui.annotation.ExperimentalComponentsApi
import framework.cortena.ui.color.ColorToken
import framework.cortena.ui.graphics.shadow.Shadow
import framework.cortena.ui.interaction.DampedAnimation
import framework.cortena.ui.primitives.Container
import framework.cortena.ui.primitives.DragTrack
import framework.cortena.ui.shape.CapsuleShape
import framework.cortena.ui.size.SizeToken
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.theme.LocalIsDark
import framework.cortena.ui.theme.toggleThumbPadding
import framework.cortena.ui.theme.toggleThumbWidth
import framework.cortena.ui.theme.toggleTrackHeight
import framework.cortena.ui.theme.toggleTrackWidth
import framework.cortena.ui.theme.value
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalComponentsApi::class)
@Composable
fun Toggle(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    activeColor: Color = Color.Unspecified,
    inactiveColor: Color = Color.Unspecified,
    thumbColor: Color = Color.Unspecified,
) {
    val colors = LocalColors.current
    val shape = CapsuleShape()
    // Toggle has one size, the Medium tier, and ignores LocalSizeToken
    val size = SizeToken.Medium

    val trackWidth = size.toggleTrackWidth
    val trackHeight = size.toggleTrackHeight
    val thumbPadding = size.toggleThumbPadding
    val thumbWidth = size.toggleThumbWidth
    val thumbHeight = trackHeight - (thumbPadding * 2)

    val resolvedActiveColor = if (activeColor.isSpecified) activeColor else Color(colors.success)
    // The separator tone, so the track stays visible on surfaceVariant cards such as ListView
    val resolvedInactiveColor =
        if (inactiveColor.isSpecified) inactiveColor else Color(colors.outlineVariant)
    val resolvedThumbColor = if (thumbColor.isSpecified) thumbColor else Color.White
    val thumbShadow = thumbShadow(resolvedThumbColor, enabled)

    // Disabled dims the track. On a dark theme the full dim would fade the inactive gray into the
    // background, so it dims less there.
    val isDark = LocalIsDark.current
    val trackActiveColor = if (enabled) resolvedActiveColor else resolvedActiveColor.disabled()
    val trackInactiveColor =
        when {
            enabled -> resolvedInactiveColor
            isDark -> resolvedInactiveColor.copy(alpha = resolvedInactiveColor.alpha * 0.6f)
            else -> resolvedInactiveColor.disabled()
        }

    val layoutDirection = LocalLayoutDirection.current
    val isLtr = layoutDirection == LayoutDirection.Ltr
    val animationScope = rememberCoroutineScope()
    var didDrag by remember { mutableStateOf(false) }

    val currentChecked by rememberUpdatedState(checked)
    val currentOnCheckedChange by rememberUpdatedState(onCheckedChange)

    // How far the thumb slides, as a fraction of the track width
    val thumbTravel = (trackWidth - thumbWidth - thumbPadding * 2) / trackWidth

    val dampedAnimation =
        remember(animationScope) {
            // The state last reported in this gesture
            var gestureChecked = false
            DampedAnimation(
                animationScope = animationScope,
                initialValue = if (checked) 1f else 0f,
                valueRange = 0f..1f,
                visibilityThreshold = 0.01f,
                initialScale = 1f,
                // With the press growth from DragTrack, the thumb swells to about 1.5x
                pressedScale = 1.3f,
                onDragStarted = { gestureChecked = currentChecked },
                onDragStopped = {
                    if (!didDrag) {
                        gestureChecked = !currentChecked
                        currentOnCheckedChange?.invoke(gestureChecked)
                    }
                    didDrag = false
                    // Moves before release() checks, so the thumb stays swollen until it lands
                    updateValue(if (gestureChecked) 1f else 0f)
                },
                onDrag = { size, dragAmount ->
                    if (!didDrag) {
                        didDrag = dragAmount.x != 0f
                    }
                    // The thumb's own travel, so it moves with the finger
                    val travel = size.width * thumbTravel
                    if (travel > 0) {
                        val delta = dragAmount.x / travel
                        val newProgress =
                            (if (isLtr) targetValue + delta else targetValue - delta).fastCoerceIn(
                                0f,
                                1f,
                            )
                        updateValue(newProgress)
                        // The state changes as the thumb crosses the middle, with the track color
                        if ((newProgress >= 0.5f) != gestureChecked) {
                            gestureChecked = !gestureChecked
                            currentOnCheckedChange?.invoke(gestureChecked)
                        }
                    }
                },
            )
        }

    LaunchedEffect(checked) {
        snapshotFlow { checked }
            .collectLatest { isChecked ->
                val target = if (isChecked) 1f else 0f
                // A drag reports its state on the way, and the thumb stays under the finger
                if (!didDrag && dampedAnimation.targetValue != target) {
                    // A change from outside, such as a shared state, swells the thumb while it
                    // travels too
                    if (enabled) dampedAnimation.press()
                    dampedAnimation.updateValue(target)
                    dampedAnimation.release()
                }
            }
    }

    // The drag gesture also handles taps, in onDragStopped
    val gestureModifier =
        if (enabled && onCheckedChange != null) dampedAnimation.modifier else Modifier

    DragTrack(
        animation = dampedAnimation,
        modifier =
            modifier
                .size(trackWidth, trackHeight)
                .semantics {
                    role = Role.Switch
                    toggleableState = ToggleableState(checked)
                }
                .then(gestureModifier),
        thumbPadding = thumbPadding,
        track = {
            Box(
                modifier =
                    Modifier.fillMaxSize().clip(shape).drawBehind {
                        val progress = dampedAnimation.progress.fastCoerceIn(0f, 1f)
                        drawRect(lerp(trackInactiveColor, trackActiveColor, progress))
                    }
            )
        },
        thumb = {
            // The thumb fades by color, not by layer alpha: an alpha layer is drawn offscreen and
            // would cut the thumb shadow off at the toggle's bounds.
            Container(
                modifier =
                    Modifier.graphicsLayer {
                            // Stretches while the value grows and squeezes while it shrinks
                            val velocity = dampedAnimation.velocity / 50f
                            scaleX = 1f / (1f - (velocity * 0.75f).fastCoerceIn(-0.2f, 0.2f))
                            scaleY = 1f - (velocity * 0.25f).fastCoerceIn(-0.2f, 0.2f)
                        }
                        .size(thumbWidth, thumbHeight),
                shape = shape,
                color =
                    when {
                        enabled -> resolvedThumbColor
                        // See-through on a dark track, and fainter than the usual 0.38 so it
                        // stands apart from the enabled thumb. On a light theme it would take on
                        // the pale track's tint, so it fades toward the surface instead.
                        isDark -> resolvedThumbColor.copy(alpha = resolvedThumbColor.alpha * 0.25f)
                        else -> lerp(Color(colors.surface), resolvedThumbColor, 0.38f)
                    },
                shadow = if (enabled) thumbShadow else thumbShadow.copy(alpha = 0.38f),
            )
        },
    )
}

private fun Color.disabled(): Color = copy(alpha = alpha * 0.38f)

@Composable
private fun thumbShadow(thumbColor: Color, enabled: Boolean): Shadow {
    val isLight = thumbColor.luminance() > 0.5f
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
