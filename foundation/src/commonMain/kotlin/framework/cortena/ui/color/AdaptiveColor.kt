/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.color

// Holds a Light + Dark color pair and resolves to the correct one based on theme
data class AdaptiveColor(val light: Long, val dark: Long) {
    fun resolve(isDark: Boolean): Long = if (isDark) dark else light
}
