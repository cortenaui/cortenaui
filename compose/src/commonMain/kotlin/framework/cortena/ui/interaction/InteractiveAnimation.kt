/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.interaction

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCoerceAtMost
import androidx.compose.ui.util.lerp
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tanh

internal fun GraphicsLayerScope.applyInteractiveAnimation(
    pressProgress: Float,
    offset: Offset,
    baseTranslationX: Float = 0f,
    baseTranslationY: Float = 0f,
    scaleXMultiplier: Float = 1f,
    scaleYMultiplier: Float = 1f,
) {
    val scale = lerp(1f, 1f + 4f.dp.toPx() / size.height, pressProgress)

    val maxOffset = size.minDimension
    val initialDerivative = 0.05f
    translationX = baseTranslationX + maxOffset * tanh(initialDerivative * offset.x / maxOffset)
    translationY = baseTranslationY + maxOffset * tanh(initialDerivative * offset.y / maxOffset)

    val maxDragScale = 4f.dp.toPx() / size.height
    val offsetAngle = atan2(offset.y, offset.x)
    scaleX =
        (scale +
            maxDragScale *
                abs(cos(offsetAngle) * offset.x / size.maxDimension) *
                (size.width / size.height).fastCoerceAtMost(1f)) * scaleXMultiplier
    scaleY =
        (scale +
            maxDragScale *
                abs(sin(offsetAngle) * offset.y / size.maxDimension) *
                (size.height / size.width).fastCoerceAtMost(1f)) * scaleYMultiplier
}
