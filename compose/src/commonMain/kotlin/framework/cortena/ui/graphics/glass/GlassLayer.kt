/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.graphics.glass

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.rememberGraphicsLayer
import framework.cortena.ui.graphics.glass.internal.GlassBackdrop
import framework.cortena.ui.graphics.glass.internal.LocalGlassBackdrop
import framework.cortena.ui.graphics.glass.internal.glassSource

/**
 * Records [background] so that glass in [content] can blur it. Every `ContentView` page already is
 * one, for its AppBar. Use it to float glass over your own content, such as an image.
 *
 * Glass inside [background] cannot sample a layer it is part of, so it draws its tint and rim
 * without the blur.
 *
 * @param background Content shown behind the glass, recorded once per frame.
 * @param modifier Applied to the box that holds both layers.
 * @param content Glass surfaces and anything else drawn over [background], laid out like a [Box].
 */
@Composable
fun GlassLayer(
    background: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val layer = rememberGraphicsLayer()
    val backdrop = remember(layer) { GlassBackdrop(layer) }

    Box(modifier = modifier) {
        Box(modifier = Modifier.glassSource(backdrop)) {
            CompositionLocalProvider(LocalGlassBackdrop provides null, content = background)
        }
        CompositionLocalProvider(LocalGlassBackdrop provides backdrop) { content() }
    }
}

// True for glass in a GlassLayer's content, which floats over the recorded background
internal val isOverGlassLayer: Boolean
    @Composable @ReadOnlyComposable get() = LocalGlassBackdrop.current != null
