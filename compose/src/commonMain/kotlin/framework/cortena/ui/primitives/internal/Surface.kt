/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.primitives.internal

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import framework.cortena.ui.graphics.glass.Glass
import framework.cortena.ui.graphics.glass.glass
import framework.cortena.ui.graphics.shadow.InnerShadow
import framework.cortena.ui.graphics.shadow.Shadow
import framework.cortena.ui.graphics.shadow.componentInnerShadow
import framework.cortena.ui.graphics.shadow.componentShadow

// Shared by Container and Pressable: drop shadow, fill or glass, then inner shadow, all in one
// shape.
internal fun Modifier.surface(
    shape: Shape,
    color: Color,
    shadow: Shadow?,
    innerShadow: InnerShadow?,
    glass: Glass?,
): Modifier =
    this.then(if (shadow != null) Modifier.componentShadow(shadow, shape) else Modifier)
        .then(
            when {
                glass != null -> Modifier.glass(glass, shape, color)
                color.isSpecified -> Modifier.background(color, shape)
                else -> Modifier
            }
        )
        .then(
            if (innerShadow != null) Modifier.componentInnerShadow(innerShadow, shape) else Modifier
        )
