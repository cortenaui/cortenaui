/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
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
import androidx.compose.ui.zIndex
import framework.cortena.ui.color.ColorToken
import framework.cortena.ui.graphics.shadow.Shadow
import framework.cortena.ui.graphics.shadow.componentShadow
import framework.cortena.ui.interaction.DampedAnimation
import framework.cortena.ui.interaction.applyInteractiveAnimation
import framework.cortena.ui.shape.CapsuleShape
import framework.cortena.ui.size.SizeToken
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.theme.LocalSizeToken
import framework.cortena.ui.theme.toggleThumbPadding
import framework.cortena.ui.theme.toggleThumbWidth
import framework.cortena.ui.theme.toggleTrackHeight
import framework.cortena.ui.theme.toggleTrackWidth
import framework.cortena.ui.theme.value
import kotlinx.coroutines.flow.collectLatest

@Composable
fun Toggle(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: SizeToken = LocalSizeToken.current,
    activeColor: Color = Color.Unspecified,
    inactiveColor: Color = Color.Unspecified,
    thumbColor: Color = Color.Unspecified,
) {
    val colors = LocalColors.current
    val shape = CapsuleShape()

    val trackWidth = size.toggleTrackWidth
    val trackHeight = size.toggleTrackHeight
    val thumbPadding = size.toggleThumbPadding
    val thumbWidth = size.toggleThumbWidth
    val thumbHeight = trackHeight - (thumbPadding * 2)

    val resolvedActiveColor = if (activeColor.isSpecified) activeColor else Color(colors.success)
    val resolvedInactiveColor =
        if (inactiveColor.isSpecified) inactiveColor else Color(colors.surfaceVariant)
    val resolvedThumbColor = if (thumbColor.isSpecified) thumbColor else Color.White
    val thumbShadow = thumbShadow(resolvedThumbColor, enabled)

    val layoutDirection = LocalLayoutDirection.current
    val isLtr = layoutDirection == LayoutDirection.Ltr
    val animationScope = rememberCoroutineScope()
    var didDrag by remember { mutableStateOf(false) }

    val currentChecked by rememberUpdatedState(checked)
    val currentOnCheckedChange by rememberUpdatedState(onCheckedChange)

    val dampedAnimation =
        remember(animationScope) {
            DampedAnimation(
                animationScope = animationScope,
                initialValue = if (checked) 1f else 0f,
                valueRange = 0f..1f,
                visibilityThreshold = 0.01f,
                initialScale = 1f,
                pressedScale = 1.15f,
                onDragStarted = {},
                onDragStopped = {
                    if (didDrag) {
                        val target = targetValue >= 0.5f
                        currentOnCheckedChange?.invoke(target)
                        updateValue(if (target) 1f else 0f)
                        didDrag = false
                    } else {
                        currentOnCheckedChange?.invoke(!currentChecked)
                    }
                },
                onDrag = { size, dragAmount ->
                    if (!didDrag) {
                        didDrag = dragAmount.x != 0f
                    }
                    val travel = size.width.toFloat() - size.height.toFloat()
                    if (travel > 0) {
                        val delta = dragAmount.x / travel
                        val newProgress = if (isLtr) targetValue + delta else targetValue - delta
                        updateValue(newProgress.fastCoerceIn(0f, 1f))
                    }
                },
            )
        }

    LaunchedEffect(checked) {
        snapshotFlow { checked }
            .collectLatest { isChecked ->
                val target = if (isChecked) 1f else 0f
                if (dampedAnimation.targetValue != target) {
                    dampedAnimation.updateValue(target)
                }
            }
    }

    val gestureModifier =
        if (enabled && onCheckedChange != null) {
            Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            dampedAnimation.press()
                            if (tryAwaitRelease()) {
                                dampedAnimation.release()
                            } else {
                                dampedAnimation.release()
                            }
                        },
                        onTap = { currentOnCheckedChange?.invoke(!currentChecked) },
                    )
                }
                .then(dampedAnimation.modifier)
        } else Modifier

    Box(
        modifier =
            modifier
                .size(trackWidth, trackHeight)
                .semantics {
                    role = Role.Switch
                    toggleableState = ToggleableState(checked)
                }
                .then(gestureModifier)
                .graphicsLayer { alpha = if (enabled) 1f else 0.38f },
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier =
                Modifier.matchParentSize().clip(shape).drawBehind {
                    val progress = dampedAnimation.progress.fastCoerceIn(0f, 1f)
                    drawRect(lerp(resolvedInactiveColor, resolvedActiveColor, progress))
                }
        )

        Box(
            modifier =
                Modifier.zIndex(1f)
                    .graphicsLayer {
                        val progress = dampedAnimation.progress.fastCoerceIn(0f, 1f)
                        val travel = (trackWidth - thumbWidth - (thumbPadding * 2)).toPx()
                        val thumbStart = if (isLtr) progress * travel else (1f - progress) * travel

                        applyInteractiveAnimation(
                            pressProgress = dampedAnimation.pressProgress,
                            offset = Offset.Zero,
                            baseTranslationX = thumbStart + thumbPadding.toPx(),
                            scaleXMultiplier = dampedAnimation.scaleX,
                            scaleYMultiplier = dampedAnimation.scaleY,
                        )
                    }
                    .componentShadow(thumbShadow, shape)
                    .clip(shape)
                    .background(resolvedThumbColor)
                    .size(thumbWidth, thumbHeight)
        )
    }
}

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
