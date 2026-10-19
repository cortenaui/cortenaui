/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.spacing

import kotlin.test.Test
import kotlin.test.assertEquals

class SpacingTest {

    @Test
    fun valuesAscendOnTheFourDpGrid() {
        val values =
            listOf(
                Spacing.None,
                Spacing.Xs,
                Spacing.Sm,
                Spacing.Md,
                Spacing.Lg,
                Spacing.Xl,
                Spacing.Xxl,
                Spacing.Xxxl,
            )
        values.forEach { assertEquals(0f, it % 4f, "$it is not a multiple of 4") }
        assertEquals(values.sorted(), values)
    }
}
