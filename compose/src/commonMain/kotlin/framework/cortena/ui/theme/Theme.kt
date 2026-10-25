/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import framework.cortena.ui.color.DarkPalette
import framework.cortena.ui.color.LightPalette
import framework.cortena.ui.color.Palette
import framework.cortena.ui.compose.resources.Res
import framework.cortena.ui.compose.resources.inter
import framework.cortena.ui.compose.resources.inter_italic
import framework.cortena.ui.motion.DefaultMotion
import framework.cortena.ui.motion.LocalMotion
import framework.cortena.ui.motion.Motion
import framework.cortena.ui.size.SizeToken
import framework.cortena.ui.spacing.Spacing
import framework.cortena.ui.typography.DefaultTypography
import framework.cortena.ui.typography.Typography
import org.jetbrains.compose.resources.Font

@Composable
fun Theme(
    themeMode: ThemeMode = ThemeMode.Auto,
    palette: Palette? = null,
    typography: Typography = DefaultTypography,
    fontFamily: FontFamily? = null,
    sizeToken: SizeToken = SizeToken.Medium,
    motion: Motion = DefaultMotion,
    content: @Composable () -> Unit,
) {
    val isDark = themeMode.isDark()
    val resolvedPalette = palette ?: if (isDark) DarkPalette else LightPalette
    val resolvedFontFamily = fontFamily ?: interFontFamily()

    CompositionLocalProvider(
        LocalIsDark provides isDark,
        LocalColors provides resolvedPalette,
        LocalTypography provides typography,
        LocalFontFamily provides resolvedFontFamily,
        LocalSpacing provides Spacing,
        LocalSizeToken provides sizeToken,
        LocalMotion provides motion,
        content = content,
    )
}

@Composable
internal fun ThemeMode.isDark(): Boolean =
    when (this) {
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
        ThemeMode.Auto -> isSystemInDarkTheme()
    }

// Inter, the default typeface. One variable file per style covers every weight from 100 to 900.
@Composable
private fun interFontFamily(): FontFamily =
    FontFamily(
        (100..900 step 100).flatMap { weight ->
            listOf(
                Font(Res.font.inter, FontWeight(weight)),
                Font(Res.font.inter_italic, FontWeight(weight), FontStyle.Italic),
            )
        }
    )
