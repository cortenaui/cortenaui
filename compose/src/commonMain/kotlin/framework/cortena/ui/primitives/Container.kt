/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.primitives

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import framework.cortena.ui.annotation.ExperimentalComponentsApi
import framework.cortena.ui.graphics.shadow.InnerShadow
import framework.cortena.ui.graphics.shadow.Shadow
import framework.cortena.ui.primitives.internal.surface
import framework.cortena.ui.shape.RectangleShape

/**
 * A box with a shape, fill, shadows, and padding. The static building block for cards, panels, and
 * any custom surface. Content is not clipped to [shape].
 *
 * @param modifier Applied outside the shadow, so size modifiers set the surface size.
 * @param shape Outline shared by the fill and both shadows.
 * @param color Fill color. [Color.Unspecified] draws no fill.
 * @param shadow Drop shadow outside [shape], or `null` for none.
 * @param innerShadow Shadow inside [shape], drawn over the content, or `null` for none.
 * @param padding Space between the surface edge and [content].
 * @param contentAlignment Alignment of [content] inside the padding.
 * @param content Content, laid out like a [Box].
 */
@ExperimentalComponentsApi
@Composable
fun Container(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = Color.Unspecified,
    shadow: Shadow? = null,
    innerShadow: InnerShadow? = null,
    padding: PaddingValues = PaddingValues(),
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier = modifier.surface(shape, color, shadow, innerShadow).padding(padding),
        contentAlignment = contentAlignment,
        content = content,
    )
}
