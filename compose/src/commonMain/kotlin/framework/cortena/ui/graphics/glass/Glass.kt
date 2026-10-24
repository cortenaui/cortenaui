/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.graphics.glass

import androidx.compose.runtime.Immutable

/**
 * A liquid glass recipe: the content behind a surface is saturated, blurred, then bent near the
 * edge like the rim of a lens. The defaults are CortenaUI's tuned recipe.
 *
 * Every value scales with the surface, so one recipe fits a small button and a large card alike: a
 * small button blurs lightly and keeps the bent edge visible.
 *
 * @param blurRadius Blur applied to the content behind, as a fraction of the shorter side (16 dp on
 *   a 256 dp surface).
 * @param refractionHeight Width of the bent band along the edge, as a fraction of half the shorter
 *   side.
 * @param refractionAmount How far the band bends the content, as a fraction of the shorter side.
 */
@Immutable
data class Glass(
    val blurRadius: Float = 0.0625f,
    val refractionHeight: Float = 0.2f,
    val refractionAmount: Float = 0.32f,
)
