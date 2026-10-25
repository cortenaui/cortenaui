/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.layout.internal

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import framework.cortena.ui.motion.LocalMotion

// Switches the theme under a fading snapshot of the old one, so the screen recolors smoothly instead
// of in one jump. Put modifier on the root of the themed content and theme it with isDark.
internal class ThemeCrossfade(initialDark: Boolean, private val layer: GraphicsLayer) {
    var isDark by mutableStateOf(initialDark)
        private set

    private var snapshot by mutableStateOf<ImageBitmap?>(null)
    private val fade = Animatable(0f)

    // Draws the content from the recorded layer, so the snapshot costs no extra pass
    val modifier: Modifier =
        Modifier.drawWithContent {
            layer.record { this@drawWithContent.drawContent() }
            drawLayer(layer)
            snapshot?.let { drawImage(it, alpha = fade.value) }
        }

    suspend fun switchTo(dark: Boolean, animationSpec: AnimationSpec<Float>) {
        if (dark == isDark) return
        // Captured before the switch, while the layer still holds the old theme
        snapshot = layer.toImageBitmap()
        isDark = dark
        fade.snapTo(1f)
        fade.animateTo(0f, animationSpec)
        snapshot = null
    }
}

@Composable
internal fun rememberThemeCrossfade(isDark: Boolean): ThemeCrossfade {
    val motion = LocalMotion.current
    val layer = rememberGraphicsLayer()
    val crossfade = remember(layer) { ThemeCrossfade(isDark, layer) }
    LaunchedEffect(isDark) {
        crossfade.switchTo(isDark, tween(motion.medium, easing = motion.standardEasing))
    }
    return crossfade
}
