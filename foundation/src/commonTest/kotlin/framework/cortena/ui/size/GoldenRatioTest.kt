/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.size

import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals

class GoldenRatioTest {

    @Test
    fun goldenRatioMatchesItsDefinition() {
        assertEquals(((1.0 + sqrt(5.0)) / 2.0).toFloat(), GoldenRatio, 1e-6f)
    }

    @Test
    fun twoStepsSpanOneGoldenRatio() {
        assertEquals(GoldenRatio, GoldenRatioStep * GoldenRatioStep, 1e-5f)
    }

    @Test
    fun fourSubStepsSpanOneGoldenRatio() {
        val subStep = GoldenRatioSubStep
        assertEquals(GoldenRatio, subStep * subStep * subStep * subStep, 1e-5f)
    }
}
