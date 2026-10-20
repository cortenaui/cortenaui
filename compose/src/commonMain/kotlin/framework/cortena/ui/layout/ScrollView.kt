/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.layout

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import framework.cortena.ui.geometry.Orientation
import framework.cortena.ui.layout.internal.BounceOverscrollEffect
import framework.cortena.ui.layout.internal.ScrollIndicator
import framework.cortena.ui.layout.internal.ScrollIndicatorMetrics
import framework.cortena.ui.motion.LocalMotion
import framework.cortena.ui.shape.CapsuleShape
import framework.cortena.ui.theme.LocalColors
import kotlinx.coroutines.launch

/**
 * Where the scroll indicator sits along the cross axis. `End` is the standard placement (right edge
 * for vertical scroll, bottom for horizontal). `Start` flips it.
 */
enum class ScrollIndicatorPosition {
    Start,
    End,
}

@Suppress("FrequentlyChangingValue")
@Composable
fun ScrollView(
    modifier: Modifier = Modifier,

    // Orientation
    orientation: Orientation = Orientation.Vertical,

    // Scroll Control
    scrollState: ScrollState = rememberScrollState(),
    enabled: Boolean = true,
    reverseLayout: Boolean = false,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),

    // Content Padding
    contentPadding: PaddingValues = PaddingValues(0.dp),

    // Scroll Indicator
    showScrollIndicator: Boolean = true,
    indicatorThickness: Dp = 3.dp,
    indicatorColor: Color = Color.Unspecified,
    indicatorShape: Shape = CapsuleShape(),
    indicatorPadding: Dp = 2.dp,
    indicatorPosition: ScrollIndicatorPosition = ScrollIndicatorPosition.End,
    autoHideIndicator: Boolean = true,
    draggableIndicator: Boolean = true,

    // Callbacks
    onScrolled: ((scrollValue: Int, maxScrollValue: Int) -> Unit)? = null,
    onReachedTop: (() -> Unit)? = null,
    onReachedBottom: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val safeModifier =
        modifier.then(
            if (orientation == Orientation.Vertical) {
                Modifier.heightIn(min = 48.dp)
            } else {
                Modifier.widthIn(min = 48.dp)
            }
        )

    val colors = LocalColors.current
    val motion = LocalMotion.current
    val resolvedIndicatorColor =
        if (indicatorColor.isSpecified) indicatorColor else Color(colors.outline)

    val bounceScope = rememberCoroutineScope()
    val overscrollEffect =
        remember(bounceScope, orientation, motion) {
            BounceOverscrollEffect(bounceScope, orientation, motion.smooth)
        }

    // Compose shows no overscroll when the state cannot scroll, so content that fits leaves the
    // drag to the parent (the page bounce in ContentView) instead of swallowing it.
    val userScrollEnabled = enabled && scrollState.maxValue > 0

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }
            .collect { value ->
                onScrolled?.invoke(value, scrollState.maxValue)
                if (value == 0) onReachedTop?.invoke()
                if (value == scrollState.maxValue && scrollState.maxValue > 0) {
                    onReachedBottom?.invoke()
                }
            }
    }

    val indicatorScope = rememberCoroutineScope()

    Box(modifier = safeModifier) {
        val scrollModifier =
            if (orientation == Orientation.Vertical) {
                Modifier.verticalScroll(
                    state = scrollState,
                    enabled = userScrollEnabled,
                    flingBehavior = flingBehavior,
                    reverseScrolling = reverseLayout,
                    overscrollEffect = overscrollEffect,
                )
            } else {
                Modifier.horizontalScroll(
                    state = scrollState,
                    enabled = userScrollEnabled,
                    flingBehavior = flingBehavior,
                    reverseScrolling = reverseLayout,
                    overscrollEffect = overscrollEffect,
                )
            }

        if (orientation == Orientation.Vertical) {
            Column(
                modifier = scrollModifier.then(overscrollEffect.overscroll).padding(contentPadding)
            ) {
                content()
            }
        } else {
            Row(
                modifier = scrollModifier.then(overscrollEffect.overscroll).padding(contentPadding)
            ) {
                content()
            }
        }

        if (showScrollIndicator) {
            val viewportPx = scrollState.viewportSize.toFloat()
            val totalContentPx = viewportPx + scrollState.maxValue
            val scrollFraction =
                if (scrollState.maxValue > 0) {
                    scrollState.value.toFloat() / scrollState.maxValue
                } else {
                    0f
                }
            val maxScroll = scrollState.maxValue
            val metrics =
                ScrollIndicatorMetrics(
                    viewportPx = viewportPx,
                    totalContentPx = totalContentPx,
                    scrollFraction = scrollFraction,
                    scrollByFraction = { fraction ->
                        if (maxScroll <= 0) return@ScrollIndicatorMetrics
                        val deltaPx = fraction * maxScroll
                        val target = (scrollState.value + deltaPx.toInt()).coerceIn(0, maxScroll)
                        indicatorScope.launch { scrollState.scrollTo(target) }
                    },
                )

            ScrollIndicator(
                metrics = metrics,
                orientation = orientation,
                thickness = indicatorThickness,
                color = resolvedIndicatorColor,
                shape = indicatorShape,
                padding = indicatorPadding,
                position = indicatorPosition,
                autoHide = autoHideIndicator,
                draggable = draggableIndicator,
                activitySignal = scrollState.value,
            )
        }
    }
}
