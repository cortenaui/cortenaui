/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.graphics.glass

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import framework.cortena.ui.graphics.glass.internal.GlassElement

actual fun Modifier.glass(glass: Glass, shape: Shape, color: Color): Modifier =
    this then GlassElement(glass, shape, color)
