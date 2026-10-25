/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.primitives

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import framework.cortena.ui.annotation.ExperimentalComponentsApi
import framework.cortena.ui.graphics.glass.Glass
import framework.cortena.ui.graphics.glass.glass
import framework.cortena.ui.graphics.shadow.InnerShadow
import framework.cortena.ui.graphics.shadow.Shadow
import framework.cortena.ui.interaction.InteractiveHighlight
import framework.cortena.ui.interaction.applyInteractiveAnimation
import framework.cortena.ui.primitives.internal.glassShadow
import framework.cortena.ui.primitives.internal.surface
import framework.cortena.ui.shape.RectangleShape

/**
 * A [Container] that responds to touch with CortenaUI's press physics and highlight. Every
 * touchable component starts here.
 *
 * While pressed, the surface grows and follows the finger, and a glow tracks the touch inside
 * [shape]. When disabled it renders at 38% alpha and ignores touches. Content is clipped to
 * [shape].
 *
 * @param onClick Called on tap.
 * @param modifier Applied outside the press motion and the shadow.
 * @param enabled When `false`, touches are ignored and the surface is drawn at 38% alpha.
 * @param onLongClick Called on long press, or `null` to treat a long press as a tap.
 * @param interactive When `false`, the surface keeps the highlight but skips the press motion.
 * @param role Accessibility role announced for the surface.
 * @param shape Outline shared by the fill, both shadows, the clip, and the highlight.
 * @param color Fill color. [Color.Unspecified] draws no fill. With [glass], the tint of the glass.
 *   The highlight is tuned from the first value.
 * @param shadow Drop shadow outside [shape]. With `null`, glass floating over a `GlassLayer` casts
 *   [Shadow.Default] in light mode, and any other surface casts none.
 * @param innerShadow Shadow inside [shape], drawn over the content, or `null` for none.
 * @param glass Liquid glass under the content, or `null` for a plain fill. See [Modifier.glass].
 * @param padding Space between the surface edge and [content].
 * @param contentAlignment Alignment of [content] inside the padding.
 * @param content Content, laid out like a [Box].
 */
@ExperimentalComponentsApi
@Composable
fun Pressable(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onLongClick: (() -> Unit)? = null,
    interactive: Boolean = true,
    role: Role = Role.Button,
    shape: Shape = RectangleShape,
    color: Color = Color.Unspecified,
    shadow: Shadow? = null,
    innerShadow: InnerShadow? = null,
    glass: Glass? = null,
    padding: PaddingValues = PaddingValues(),
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit,
) {
    val animationScope = rememberCoroutineScope()
    // Changes to the color must not destroy and recreate the InteractiveHighlight
    val highlight =
        remember(animationScope) {
            InteractiveHighlight(animationScope = animationScope, color = color)
        }

    Box(
        modifier =
            modifier
                .graphicsLayer {
                    if (interactive) {
                        applyInteractiveAnimation(
                            pressProgress = highlight.pressProgress,
                            offset = highlight.offset,
                        )
                    }
                    alpha = if (enabled) 1f else 0.38f
                }
                .surface(shape, color, glassShadow(shadow, glass), innerShadow, glass)
                .clip(shape)
                .then(if (enabled) highlight.gestureModifier else Modifier)
                .combinedClickable(
                    interactionSource = null,
                    indication = null,
                    enabled = enabled,
                    role = role,
                    onClick = onClick,
                    onLongClick = onLongClick,
                )
                .then(if (enabled) highlight.modifier else Modifier)
                .padding(padding),
        contentAlignment = contentAlignment,
        content = content,
    )
}
