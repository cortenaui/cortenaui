/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import framework.cortena.ui.annotation.ExperimentalComponentsApi
import framework.cortena.ui.components.Button
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.graphics.glass.Glass
import framework.cortena.ui.graphics.glass.GlassLayer
import framework.cortena.ui.graphics.shadow.InnerShadow
import framework.cortena.ui.graphics.shadow.Shadow
import framework.cortena.ui.interaction.DampedAnimation
import framework.cortena.ui.primitives.Container
import framework.cortena.ui.primitives.DragTrack
import framework.cortena.ui.primitives.Pressable
import framework.cortena.ui.shape.CapsuleShape
import framework.cortena.ui.shape.RoundedShape
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.theme.LocalSizeToken
import framework.cortena.ui.theme.LocalSpacing
import framework.cortena.ui.theme.componentHeight
import kotlin.math.roundToInt

@OptIn(ExperimentalComponentsApi::class)
@Composable
fun PrimitivesDemo() {
    val colors = LocalColors.current
    val spacing = LocalSpacing.current
    val shape = RoundedShape(spacing.Lg.dp)
    val padding = PaddingValues(spacing.Md.dp)
    var taps by remember { mutableIntStateOf(0) }

    Text("Container")
    Container(
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        color = Color(colors.surfaceVariant),
        shadow = Shadow.Default,
        padding = padding,
    ) {
        Text("Card: shape, fill, shadow, and padding")
    }
    Container(
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        color = Color(colors.surfaceVariant),
        innerShadow = InnerShadow.Default,
        padding = padding,
    ) {
        Text("Inset well: inner shadow")
    }

    Text("Pressable")
    Pressable(
        onClick = { taps++ },
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        color = Color(colors.surfaceVariant),
        padding = padding,
    ) {
        Text("Tapped $taps times")
    }
    Pressable(
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        enabled = false,
        shape = shape,
        color = Color(colors.surfaceVariant),
        padding = padding,
    ) {
        Text("Disabled")
    }

    Text("Glass")
    GlassDemo()

    Text("DragTrack")
    LevelBar()
}

// Glass blurs only what a GlassLayer records, so the surfaces float over their own background
@OptIn(ExperimentalComponentsApi::class)
@Composable
private fun GlassDemo() {
    val colors = LocalColors.current
    val spacing = LocalSpacing.current

    GlassLayer(
        background = {
            Column(
                modifier =
                    Modifier.fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedShape(spacing.Lg.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(colors.primary), Color(colors.accent))
                            )
                        ),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                repeat(4) {
                    Text(
                        "Liquid glass",
                        color = Color(colors.onPrimary),
                        role = TextRole.HeadlineLarge,
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(spacing.Lg.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Container(
                modifier = Modifier.size(144.dp),
                shape = RoundedShape(spacing.Xl.dp),
                glass = Glass(),
            )
            Button(onClick = {}) { Text("Glass") }
        }
    }
}

// A custom control built only from primitives: drag anywhere on the bar to move the thumb.
@OptIn(ExperimentalComponentsApi::class)
@Composable
private fun LevelBar() {
    val colors = LocalColors.current
    val spacing = LocalSpacing.current
    val height = LocalSizeToken.current.componentHeight
    val direction by
        rememberUpdatedState(if (LocalLayoutDirection.current == LayoutDirection.Ltr) 1f else -1f)
    val animationScope = rememberCoroutineScope()
    val level =
        remember(animationScope) {
            DampedAnimation(
                animationScope = animationScope,
                initialValue = 0.5f,
                valueRange = 0f..1f,
                visibilityThreshold = 0.001f,
                initialScale = 1f,
                pressedScale = 1.2f,
                onDragStarted = {},
                onDragStopped = {},
                onDrag = { size, dragAmount ->
                    updateValue(targetValue + direction * dragAmount.x / size.width)
                },
            )
        }

    Text("Level: ${(level.targetValue * 100).roundToInt()}%")
    DragTrack(
        animation = level,
        modifier = Modifier.fillMaxWidth().height(height).then(level.modifier),
        thumbPadding = spacing.Xs.dp,
        track = {
            Container(
                modifier = Modifier.fillMaxSize(),
                shape = CapsuleShape(),
                color = Color(colors.surfaceVariant),
            )
        },
        thumb = {
            Container(
                modifier = Modifier.size(height - spacing.Xs.dp * 2),
                shape = CapsuleShape(),
                color = Color(colors.primary),
                shadow = Shadow.Default,
            )
        },
    )
}
