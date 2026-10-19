/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.interaction

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import framework.cortena.ui.color.ColorToken

internal fun interactiveHighlightColor(sourceColor: Color): Color {
    if (!sourceColor.isSpecified) {
        return Color(ColorToken.Gray500.light)
    }

    val isVeryLight = sourceColor.luminance() > 0.78f
    val targetColor = if (isVeryLight) Color.Black else Color.White
    val fraction = if (isVeryLight) 0.18f else 0.36f
    return lerp(sourceColor, targetColor, fraction)
}
