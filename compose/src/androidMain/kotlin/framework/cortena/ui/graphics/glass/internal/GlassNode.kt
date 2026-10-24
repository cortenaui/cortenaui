/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.graphics.glass.internal

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.graphics.Shader
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.node.requireGraphicsContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import framework.cortena.ui.graphics.glass.Glass
import framework.cortena.ui.shape.ComponentShape
import kotlin.math.ceil

internal class GlassElement(val glass: Glass, val shape: Shape, val color: Color) :
    ModifierNodeElement<GlassNode>() {

    override fun create(): GlassNode = GlassNode(glass, shape, color)

    override fun update(node: GlassNode) = node.update(glass, shape, color)

    override fun equals(other: Any?): Boolean =
        other is GlassElement &&
            other.glass == glass &&
            other.shape == shape &&
            other.color == color

    override fun hashCode(): Int {
        var result = glass.hashCode()
        result = 31 * result + shape.hashCode()
        result = 31 * result + color.hashCode()
        return result
    }
}

internal class GlassNode(
    private var glass: Glass,
    private var shape: Shape,
    private var color: Color,
) :
    DrawModifierNode,
    GlobalPositionAwareModifierNode,
    CompositionLocalConsumerModifierNode,
    Modifier.Node() {

    // Never equal: the same coordinates object reports a new position after a move
    private var coordinates: LayoutCoordinates? by mutableStateOf(null, neverEqualPolicy())

    // Created on first use, so glass with nothing to blur never allocates a layer
    private var backdropLayer: GraphicsLayer? = null
    private var effectValid = false
    private var padding = 0
    private val sourceToLocal = Matrix()

    // Everything below depends on the size, so it is rebuilt only when the size changes
    private var outline: Outline? = null
    private val outlinePath = Path()
    private var outlineSize = Size.Unspecified
    private var outlineDensity = 0f
    private var outlineLayoutDirection = LayoutDirection.Ltr
    private var rim = Stroke()

    fun update(glass: Glass, shape: Shape, color: Color) {
        if (glass != this.glass || shape != this.shape) outline = null
        this.glass = glass
        this.shape = shape
        this.color = color
        invalidateDraw()
    }

    override fun ContentDrawScope.draw() {
        val outline = resolveOutline()
        val source = currentValueOf(LocalGlassBackdrop)
        val coordinates = coordinates
        val sourceCoordinates = source?.coordinates

        if (
            source != null &&
                coordinates != null &&
                coordinates.isAttached &&
                sourceCoordinates != null &&
                sourceCoordinates.isAttached
        ) {
            drawBackdrop(source.layer, coordinates, sourceCoordinates)
            if (color.isSpecified) {
                drawOutline(outline, color.copy(alpha = color.alpha * TintAlpha))
            }
        } else if (color.isSpecified) {
            drawOutline(outline, color.copy(alpha = color.alpha * FillAlpha))
        }

        // The stroke is centered on the edge, so the clip keeps only its inner half
        clipPath(outlinePath) { drawOutline(outline, rimColor(color), style = rim) }
        drawContent()
    }

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        // Glass with nothing to blur skips the write, so a scrolling list does not redraw it
        if (currentValueOf(LocalGlassBackdrop) != null) this.coordinates = coordinates
    }

    override fun onDetach() {
        backdropLayer?.let { requireGraphicsContext().releaseGraphicsLayer(it) }
        backdropLayer = null
        coordinates = null
        outline = null
    }

    private fun DrawScope.resolveOutline(): Outline {
        val cached = outline
        if (
            cached != null &&
                size == outlineSize &&
                density == outlineDensity &&
                layoutDirection == outlineLayoutDirection
        ) {
            return cached
        }
        outlineSize = size
        outlineDensity = density
        outlineLayoutDirection = layoutDirection
        effectValid = false
        rim = Stroke(width = RimWidth.toPx() * 2f)
        return shape.createOutline(size, layoutDirection, this).also {
            outline = it
            outlinePath.rewind()
            outlinePath.addOutline(it)
        }
    }

    private fun ContentDrawScope.drawBackdrop(
        source: GraphicsLayer,
        coordinates: LayoutCoordinates,
        sourceCoordinates: LayoutCoordinates,
    ) {
        val layer =
            backdropLayer
                ?: requireGraphicsContext().createGraphicsLayer().also { backdropLayer = it }
        if (!effectValid) {
            layer.renderEffect = createEffect()
            effectValid = true
        }

        // Maps the recorded content onto this surface, including any press scale on the way
        sourceToLocal.reset()
        coordinates.transformFrom(sourceCoordinates, sourceToLocal)

        val padding = padding
        layer.record(
            IntSize(ceil(size.width).toInt() + padding * 2, ceil(size.height).toInt() + padding * 2)
        ) {
            translate(padding.toFloat(), padding.toFloat()) {
                withTransform({ transform(sourceToLocal) }) { drawLayer(source) }
            }
        }
        layer.topLeft = IntOffset(-padding, -padding)
        clipPath(outlinePath) { drawLayer(layer) }
    }

    private fun DrawScope.createEffect(): androidx.compose.ui.graphics.RenderEffect {
        val minDimension = size.minDimension
        val blurRadius = glass.blurRadius * minDimension
        val refractionHeight = glass.refractionHeight * minDimension * 0.5f
        val refractionAmount = glass.refractionAmount * minDimension

        var effect = RenderEffect.createColorFilterEffect(VibrancyFilter)
        var padding = 0f
        if (blurRadius > 0f) {
            // The blur reads past the edge, so the content is recorded with that much around it
            padding = blurRadius
            effect =
                RenderEffect.createBlurEffect(blurRadius, blurRadius, effect, Shader.TileMode.CLAMP)
        }

        val corners = (shape as? ComponentShape)?.corners(size, layoutDirection, this)
        if (corners != null && refractionHeight > 0f && refractionAmount > 0f) {
            // The edge band samples inward, so it needs less of that extra area
            padding = (padding - refractionHeight).coerceAtLeast(0f)
            val offset = -ceil(padding)
            val lens = RefractionShader.apply {
                setFloatUniform("size", size.width, size.height)
                setFloatUniform("offset", offset, offset)
                setFloatUniform(
                    "cornerRadii",
                    corners.topLeft,
                    corners.topRight,
                    corners.bottomRight,
                    corners.bottomLeft,
                )
                setFloatUniform("refractionHeight", refractionHeight)
                setFloatUniform("refractionAmount", -refractionAmount)
            }
            effect =
                RenderEffect.createChainEffect(
                    RenderEffect.createRuntimeShaderEffect(lens, "content"),
                    effect,
                )
        }

        this@GlassNode.padding = ceil(padding).toInt()
        return effect.asComposeRenderEffect()
    }
}

// Share of the tint over the blur, low enough for the content behind to show through
private const val TintAlpha = 0.6f

// With nothing to blur, the fill stays almost solid so its content keeps its contrast
private const val FillAlpha = 0.9f

private val RimWidth = 0.5.dp

// Clear glass has no color of its own, so its rim is plain light
private val ClearRimColor = Color.White.copy(alpha = 0.38f)

// A lighter shade of the tint, as faint as the tint itself, so the rim blends with the surface
private fun rimColor(tint: Color): Color =
    if (tint.isSpecified) {
        lerp(tint.copy(alpha = 1f), Color.White, 0.5f).copy(alpha = tint.alpha * 0.5f)
    } else {
        ClearRimColor
    }

// Saturation 1.5x brings back the color that the blur washes out
private val VibrancyFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(1.5f) })

// Compiled once per process: each RenderEffect keeps the uniform values it was created with, so
// every glass surface can share this one shader
private val RefractionShader by lazy(LazyThreadSafetyMode.NONE) { RuntimeShader(RefractionSksl) }

// Bends the content inside a band along the edge of a rounded rectangle, as if through the rim of
// a lens. Ported from androidliquidglass, without the chromatic aberration pass.
private const val RefractionSksl =
    """
uniform shader content;

uniform float2 size;
uniform float2 offset;
uniform float4 cornerRadii;
uniform float refractionHeight;
uniform float refractionAmount;

float radiusAt(float2 coord, float4 radii) {
    if (coord.x >= 0.0) {
        return coord.y <= 0.0 ? radii.y : radii.z;
    }
    return coord.y <= 0.0 ? radii.x : radii.w;
}

float sdRoundedRect(float2 coord, float2 halfSize, float radius) {
    float2 cornerCoord = abs(coord) - (halfSize - float2(radius));
    float outside = length(max(cornerCoord, 0.0)) - radius;
    float inside = min(max(cornerCoord.x, cornerCoord.y), 0.0);
    return outside + inside;
}

float2 gradSdRoundedRect(float2 coord, float2 halfSize, float radius) {
    float2 cornerCoord = abs(coord) - (halfSize - float2(radius));
    if (cornerCoord.x >= 0.0 || cornerCoord.y >= 0.0) {
        return sign(coord) * normalize(max(cornerCoord, 0.0));
    }
    float gradX = step(cornerCoord.y, cornerCoord.x);
    return sign(coord) * float2(gradX, 1.0 - gradX);
}

float circleMap(float x) {
    return 1.0 - sqrt(1.0 - x * x);
}

half4 main(float2 coord) {
    float2 halfSize = size * 0.5;
    float2 centeredCoord = (coord + offset) - halfSize;
    float radius = radiusAt(centeredCoord, cornerRadii);

    float sd = sdRoundedRect(centeredCoord, halfSize, radius);
    if (-sd >= refractionHeight) {
        return content.eval(coord);
    }
    sd = min(sd, 0.0);

    float d = circleMap(1.0 - -sd / refractionHeight) * refractionAmount;
    float gradRadius = min(radius * 1.5, min(halfSize.x, halfSize.y));
    float2 grad = normalize(
        gradSdRoundedRect(centeredCoord, halfSize, gradRadius) + normalize(centeredCoord)
    );
    return content.eval(coord + d * grad);
}
"""
