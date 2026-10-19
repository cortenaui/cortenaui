/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import framework.cortena.ui.geometry.Orientation
import framework.cortena.ui.theme.LocalColors

@Composable
fun Separator(
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Horizontal,
    thickness: Dp = 1.dp,
    color: Color = Color.Unspecified,
    indent: Dp = 0.dp,
) {
    val colors = LocalColors.current
    val resolvedColor = if (color.isSpecified) color else Color(colors.outlineVariant)

    val layoutModifier =
        if (orientation == Orientation.Horizontal) {
            Modifier.fillMaxWidth().height(thickness).padding(start = indent, end = indent)
        } else {
            Modifier.fillMaxHeight().width(thickness).padding(top = indent, bottom = indent)
        }

    Box(modifier = modifier.then(layoutModifier).background(resolvedColor))
}
