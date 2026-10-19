/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import framework.cortena.ui.theme.LocalColors

@Composable
fun Body(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val colors = LocalColors.current
    Column(modifier = modifier.fillMaxSize().background(Color(colors.surface))) { content() }
}
