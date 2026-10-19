/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.typography

import framework.cortena.ui.size.GoldenRatio
import framework.cortena.ui.size.GoldenRatioSubStep
import kotlin.test.Test
import kotlin.test.assertEquals

class TypeScaleTest {

    // Every role, smallest to largest.
    private val scale =
        listOf(
            TypeScale.BodySmall,
            TypeScale.BodyMedium,
            TypeScale.BodyLarge,
            TypeScale.TitleSmall,
            TypeScale.TitleMedium,
            TypeScale.TitleLarge,
            TypeScale.HeadlineSmall,
            TypeScale.HeadlineMedium,
            TypeScale.HeadlineLarge,
            TypeScale.DisplaySmall,
            TypeScale.DisplayMedium,
            TypeScale.DisplayLarge,
        )

    @Test
    fun bodyMediumIsTheAnchor() {
        assertEquals(14f, TypeScale.BodyMedium)
    }

    @Test
    fun consecutiveRolesStepByOneSubStep() {
        scale.zipWithNext().forEach { (smaller, larger) ->
            assertEquals(GoldenRatioSubStep, larger / smaller, 1e-5f)
        }
    }

    @Test
    fun everyFourthRoleIsOneGoldenRatioLarger() {
        assertEquals(GoldenRatio, TypeScale.TitleLarge / TypeScale.BodyMedium, 1e-4f)
        assertEquals(GoldenRatio, TypeScale.DisplaySmall / TypeScale.TitleLarge, 1e-4f)
    }
}
