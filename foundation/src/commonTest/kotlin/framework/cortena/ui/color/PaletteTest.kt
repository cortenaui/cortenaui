/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.color

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PaletteTest {

    @Test
    fun adaptiveColorResolvesPerTheme() {
        val color = AdaptiveColor(light = 0xFFFFFFFF, dark = 0xFF000000)
        assertEquals(0xFFFFFFFF, color.resolve(isDark = false))
        assertEquals(0xFF000000, color.resolve(isDark = true))
    }

    @Test
    fun lightPaletteDrawsDarkContentOnLightSurfaces() {
        assertTrue(brightness(LightPalette.onSurface) < brightness(LightPalette.surface))
        assertTrue(brightness(LightPalette.onBackground) < brightness(LightPalette.background))
    }

    @Test
    fun darkPaletteDrawsLightContentOnDarkSurfaces() {
        assertTrue(brightness(DarkPalette.onSurface) > brightness(DarkPalette.surface))
        assertTrue(brightness(DarkPalette.onBackground) > brightness(DarkPalette.background))
    }

    // Rough perceived brightness of an ARGB color, enough to order two colors.
    private fun brightness(argb: Long): Float {
        val red = (argb shr 16 and 0xFF).toFloat()
        val green = (argb shr 8 and 0xFF).toFloat()
        val blue = (argb and 0xFF).toFloat()
        return 0.2126f * red + 0.7152f * green + 0.0722f * blue
    }
}
