/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.graphics.shadow.internal

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

@Immutable
internal class ShapeProvider(val shapeBlock: () -> Shape) {

    private var shapeCache: Shape? = null
    private var outlineCache: Outline? = null
    private var sizeCache: Size = Size.Unspecified
    private var layoutDirectionCache: LayoutDirection? = null
    private var densityCache: Float? = null

    val innerShape: Shape
        get() = shapeBlock()

    val shape =
        object : Shape {
            override fun createOutline(
                size: Size,
                layoutDirection: LayoutDirection,
                density: Density,
            ): Outline {
                val shape = shapeBlock()
                if (shapeCache != shape) {
                    shapeCache = shape
                    outlineCache = null
                }
                if (
                    outlineCache == null ||
                        sizeCache != size ||
                        layoutDirectionCache != layoutDirection ||
                        densityCache != density.density
                ) {
                    sizeCache = size
                    layoutDirectionCache = layoutDirection
                    densityCache = density.density
                    outlineCache = shape.createOutline(size, layoutDirection, density)
                }

                return outlineCache!!
            }
        }
}
