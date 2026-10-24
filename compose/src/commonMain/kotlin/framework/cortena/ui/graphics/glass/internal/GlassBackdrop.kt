/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.graphics.glass.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw

// The content recorded behind glass, and where it sits on screen
internal class GlassBackdrop(val layer: GraphicsLayer) {
    // Never equal: the same coordinates object reports a new position after a move
    var coordinates: LayoutCoordinates? by mutableStateOf(null, neverEqualPolicy())
}

internal val LocalGlassBackdrop = staticCompositionLocalOf<GlassBackdrop?> { null }

internal fun Modifier.glassSource(backdrop: GlassBackdrop): Modifier =
    this then GlassSourceElement(backdrop)

private class GlassSourceElement(val backdrop: GlassBackdrop) :
    ModifierNodeElement<GlassSourceNode>() {

    override fun create(): GlassSourceNode = GlassSourceNode(backdrop)

    override fun update(node: GlassSourceNode) {
        node.backdrop = backdrop
        node.invalidateDraw()
    }

    override fun equals(other: Any?): Boolean =
        other is GlassSourceElement && other.backdrop == backdrop

    override fun hashCode(): Int = backdrop.hashCode()
}

private class GlassSourceNode(var backdrop: GlassBackdrop) :
    DrawModifierNode, GlobalPositionAwareModifierNode, Modifier.Node() {

    // Drawn from the recorded layer instead of twice, so the content costs no extra pass
    override fun ContentDrawScope.draw() {
        backdrop.layer.record { this@draw.drawContent() }
        drawLayer(backdrop.layer)
    }

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        backdrop.coordinates = coordinates
    }

    override fun onDetach() {
        backdrop.coordinates = null
    }
}
