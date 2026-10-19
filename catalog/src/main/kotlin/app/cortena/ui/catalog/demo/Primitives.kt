/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog.demo

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import framework.cortena.ui.annotation.ExperimentalComponentsApi
import framework.cortena.ui.components.Text
import framework.cortena.ui.graphics.shadow.InnerShadow
import framework.cortena.ui.graphics.shadow.Shadow
import framework.cortena.ui.primitives.Container
import framework.cortena.ui.primitives.Pressable
import framework.cortena.ui.shape.RoundedShape
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.theme.LocalSpacing

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
        color = Color(colors.surface),
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
}
