/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.interaction

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope

/**
 * Press highlight for touchable surfaces: a tint plus a soft glow that follows the finger.
 *
 * Put [gestureModifier] where the element receives touches and [modifier] after the clip, so the
 * glow stays inside the shape. Pass [pressProgress] and [offset] to [applyInteractiveAnimation] for
 * the matching press motion. Create it in `remember(animationScope)` with a scope from
 * `rememberCoroutineScope()`. On Android the glow is an AGSL `RuntimeShader`.
 *
 * @param animationScope Scope that runs the animations.
 * @param color Surface color under the highlight. Dark colors get a lighter highlight and very
 *   light colors a darker one. [Color.Unspecified] uses a neutral gray.
 * @param position Maps the element size and the pointer position to the glow center. Defaults to
 *   the pointer position.
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class InteractiveHighlight(
    animationScope: CoroutineScope,
    color: Color = Color.Unspecified,
    position: (size: Size, offset: Offset) -> Offset = { _, offset -> offset },
) {
    /** `0` at rest, `1` while pressed. */
    val pressProgress: Float

    /** Drag distance from the press origin in pixels. */
    val offset: Offset

    /** Draws the tint and the glow over the content. */
    val modifier: Modifier

    /** Tracks presses and drags. Leave it off while the element is disabled. */
    val gestureModifier: Modifier
}
