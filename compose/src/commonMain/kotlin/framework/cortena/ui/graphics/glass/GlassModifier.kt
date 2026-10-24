/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.graphics.glass

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

/**
 * Draws [glass] in [shape] behind the content: the blurred content recorded by the nearest
 * [GlassLayer], then [color] as a tint, then a thin rim of light along the edge.
 *
 * Over recorded content, [color] tints the glass at 60% of its alpha, so the content behind shows
 * through. Without recorded content behind (a surface inside the recorded content itself, or
 * outside any [GlassLayer]), [color] is drawn at 90% of its alpha under the rim. The rim takes a
 * lighter shade of [color], so it blends with the surface.
 *
 * @param glass Blur and refraction recipe.
 * @param shape Outline of the glass, also used to bend the content along its edge.
 * @param color Tint, or [Color.Unspecified] for clear glass.
 */
expect fun Modifier.glass(glass: Glass, shape: Shape, color: Color = Color.Unspecified): Modifier
