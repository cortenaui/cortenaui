/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.color

/**
 * CortenaUI — Palette
 *
 * Semantic color roles. Components consume these roles, not raw [ColorTokens]. This decouples
 * components from specific palette values — swapping a theme only requires providing a different
 * [Palette] instance.
 *
 * Each property is a Long (ARGB) to stay framework-agnostic. Compose layer converts these to
 * androidx.compose.ui.graphics.Color.
 */
data class Palette(
    val primary: Long,
    val onPrimary: Long,
    val primaryContainer: Long,
    val onPrimaryContainer: Long,
    val secondary: Long,
    val onSecondary: Long,
    val secondaryContainer: Long,
    val onSecondaryContainer: Long,
    val accent: Long,
    val onAccent: Long,
    val accentContainer: Long,
    val onAccentContainer: Long,
    val success: Long,
    val onSuccess: Long,
    val successContainer: Long,
    val onSuccessContainer: Long,
    val warning: Long,
    val onWarning: Long,
    val warningContainer: Long,
    val onWarningContainer: Long,
    val error: Long,
    val onError: Long,
    val errorContainer: Long,
    val onErrorContainer: Long,
    val background: Long,
    val onBackground: Long,
    val surface: Long,
    val onSurface: Long,
    val surfaceVariant: Long,
    val onSurfaceVariant: Long,
    val outline: Long,
    val outlineVariant: Long,
    val scrim: Long,
    val inverseSurface: Long,
    val inverseOnSurface: Long,
    val inversePrimary: Long,
)

// Built-in palettes
val LightPalette =
    Palette(
        // Primary — Blue
        primary = ColorToken.Blue500.resolve(isDark = false),
        onPrimary = ColorToken.White.light,
        primaryContainer = ColorToken.Blue100.resolve(isDark = false),
        onPrimaryContainer = ColorToken.Blue800.resolve(isDark = false),
        inversePrimary = ColorToken.Blue300.resolve(isDark = false),

        // Secondary — Gray
        secondary = ColorToken.Gray500.resolve(isDark = false),
        onSecondary = ColorToken.White.light,
        secondaryContainer = ColorToken.Gray100.resolve(isDark = false),
        onSecondaryContainer = ColorToken.Gray800.resolve(isDark = false),

        // Accent — Orange
        accent = ColorToken.Orange500.resolve(isDark = false),
        onAccent = ColorToken.White.light,
        accentContainer = ColorToken.Orange100.resolve(isDark = false),
        onAccentContainer = ColorToken.Orange800.resolve(isDark = false),

        // Success — Green
        success = ColorToken.Green400.resolve(isDark = false),
        onSuccess = ColorToken.Green50.resolve(isDark = false),
        successContainer = ColorToken.Green100.resolve(isDark = false),
        onSuccessContainer = ColorToken.Green800.resolve(isDark = false),

        // Warning — Yellow
        warning = ColorToken.Yellow400.resolve(isDark = false),
        onWarning = ColorToken.Yellow50.resolve(isDark = false),
        warningContainer = ColorToken.Yellow100.resolve(isDark = false),
        onWarningContainer = ColorToken.Yellow800.resolve(isDark = false),

        // Error — Red
        error = ColorToken.Red400.resolve(isDark = false),
        onError = ColorToken.White.light,
        errorContainer = ColorToken.Red100.resolve(isDark = false),
        onErrorContainer = ColorToken.Red800.resolve(isDark = false),

        // Background & Surface
        background = ColorToken.White.light,
        onBackground = ColorToken.Gray950.resolve(isDark = false),
        surface = ColorToken.White.light,
        onSurface = ColorToken.Gray900.resolve(isDark = false),

        // surfaceVariant
        surfaceVariant = 0xFFF2F2F7,
        onSurfaceVariant = ColorToken.Gray600.resolve(isDark = false),

        // Inverse
        inverseSurface = ColorToken.Gray900.resolve(isDark = false),
        inverseOnSurface = ColorToken.Gray100.resolve(isDark = false),

        // Outline
        outline = 0xFFC6C6C8,
        outlineVariant = ColorToken.Gray200.resolve(isDark = false),

        // Scrim
        scrim = ColorToken.Black.light,
    )

val DarkPalette =
    Palette(
        // Primary — Blue
        primary = ColorToken.Blue500.resolve(isDark = true),
        onPrimary = ColorToken.White.dark,
        primaryContainer = ColorToken.Blue900.resolve(isDark = true),
        onPrimaryContainer = ColorToken.Blue200.resolve(isDark = true),
        inversePrimary = ColorToken.Blue600.resolve(isDark = true),

        // Secondary — Gray
        secondary = ColorToken.Gray400.resolve(isDark = true),
        onSecondary = ColorToken.Black.dark,
        secondaryContainer = ColorToken.Gray800.resolve(isDark = true),
        onSecondaryContainer = ColorToken.Gray200.resolve(isDark = true),

        // Accent — Orange
        accent = ColorToken.Orange500.resolve(isDark = true),
        onAccent = ColorToken.Black.dark,
        accentContainer = ColorToken.Orange800.resolve(isDark = true),
        onAccentContainer = ColorToken.Orange200.resolve(isDark = true),

        // Success — Green
        success = ColorToken.Green400.resolve(isDark = true),
        onSuccess = ColorToken.Green50.resolve(isDark = true),
        successContainer = ColorToken.Green100.resolve(isDark = true),
        onSuccessContainer = ColorToken.Green800.resolve(isDark = true),

        // Warning — Yellow
        warning = ColorToken.Yellow400.resolve(isDark = true),
        onWarning = ColorToken.Yellow50.resolve(isDark = true),
        warningContainer = ColorToken.Yellow100.resolve(isDark = true),
        onWarningContainer = ColorToken.Yellow800.resolve(isDark = true),

        // Error — Red
        error = ColorToken.Red400.resolve(isDark = true),
        onError = ColorToken.White.dark,
        errorContainer = ColorToken.Red900.resolve(isDark = true),
        onErrorContainer = ColorToken.Red200.resolve(isDark = true),

        // Background & Surface
        background = ColorToken.Black.dark,
        onBackground = ColorToken.Gray50.resolve(isDark = true),
        surface = ColorToken.Black.dark,
        onSurface = ColorToken.Gray100.resolve(isDark = true),

        // surfaceVariant
        surfaceVariant = 0xFF1C1C1E,
        onSurfaceVariant = ColorToken.Gray400.resolve(isDark = true),

        // Inverse
        inverseSurface = ColorToken.Gray100.resolve(isDark = true),
        inverseOnSurface = ColorToken.Gray900.resolve(isDark = true),

        // Outline
        outline = 0xFF38383A,
        outlineVariant = ColorToken.Gray800.resolve(isDark = true),

        // Scrim
        scrim = ColorToken.Black.dark,
    )
