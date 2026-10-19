/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.size

import kotlin.test.Test
import kotlin.test.assertEquals

class SizeScaleTest {

    // Small, Medium, and Large values of every dimension axis.
    private val axes =
        listOf(
            Triple(SizeScale.HeightSmall, SizeScale.HeightMedium, SizeScale.HeightLarge),
            Triple(
                SizeScale.HorizontalPaddingSmall,
                SizeScale.HorizontalPaddingMedium,
                SizeScale.HorizontalPaddingLarge,
            ),
            Triple(SizeScale.IconSizeSmall, SizeScale.IconSizeMedium, SizeScale.IconSizeLarge),
            Triple(
                SizeScale.ContentGapSmall,
                SizeScale.ContentGapMedium,
                SizeScale.ContentGapLarge,
            ),
            Triple(
                SizeScale.ToggleTrackWidthSmall,
                SizeScale.ToggleTrackWidthMedium,
                SizeScale.ToggleTrackWidthLarge,
            ),
            Triple(
                SizeScale.ToggleTrackHeightSmall,
                SizeScale.ToggleTrackHeightMedium,
                SizeScale.ToggleTrackHeightLarge,
            ),
            Triple(
                SizeScale.ToggleThumbPaddingSmall,
                SizeScale.ToggleThumbPaddingMedium,
                SizeScale.ToggleThumbPaddingLarge,
            ),
            Triple(
                SizeScale.ToggleThumbWidthSmall,
                SizeScale.ToggleThumbWidthMedium,
                SizeScale.ToggleThumbWidthLarge,
            ),
            Triple(
                SizeScale.SliderIndicatorWidthSmall,
                SizeScale.SliderIndicatorWidthMedium,
                SizeScale.SliderIndicatorWidthLarge,
            ),
            Triple(
                SizeScale.SliderTrackHeightSmall,
                SizeScale.SliderTrackHeightMedium,
                SizeScale.SliderTrackHeightLarge,
            ),
        )

    @Test
    fun everyTierStepsByOneGoldenRatioStep() {
        for ((small, medium, large) in axes) {
            assertEquals(GoldenRatioStep, medium / small, 1e-5f)
            assertEquals(GoldenRatioStep, large / medium, 1e-5f)
        }
    }
}
