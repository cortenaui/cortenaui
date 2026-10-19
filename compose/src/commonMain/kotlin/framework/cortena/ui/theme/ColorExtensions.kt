/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import framework.cortena.ui.color.AdaptiveColor

// Auto-resolve an adaptive color to the correct compose color for the active theme
@Composable fun AdaptiveColor.value(): Color = Color(resolve(LocalIsDark.current))
