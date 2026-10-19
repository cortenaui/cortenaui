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

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class InteractiveHighlight(
    animationScope: CoroutineScope,
    color: Color = Color.Unspecified,
    position: (size: Size, offset: Offset) -> Offset = { _, offset -> offset },
) {
    val pressProgress: Float
    val offset: Offset
    val modifier: Modifier
    val gestureModifier: Modifier
}
