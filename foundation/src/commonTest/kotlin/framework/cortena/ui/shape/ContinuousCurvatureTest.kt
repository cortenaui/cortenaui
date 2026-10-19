/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.shape

import kotlin.test.Test
import kotlin.test.assertEquals

class ContinuousCurvatureTest {

    // Records every drawing command and point so paths can be compared.
    private class RecordingReceiver : CornerPathReceiver {
        val commands = mutableListOf<String>()
        val points = mutableListOf<Pair<Float, Float>>()

        override fun moveTo(x: Float, y: Float) {
            commands += "M"
            points += x to y
        }

        override fun lineTo(x: Float, y: Float) {
            commands += "L"
            points += x to y
        }

        override fun cubicTo(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float) {
            commands += "C"
            points += listOf(x1 to y1, x2 to y2, x3 to y3)
        }

        override fun close() {
            commands += "Z"
        }
    }

    @Test
    fun zeroRadiiEmitAPlainRectangle() {
        val receiver = RecordingReceiver()
        ContinuousCurvature.emit(100f, 60f, 0f, 0f, 0f, 0f, receiver)

        assertEquals(listOf("M", "L", "L", "L", "Z"), receiver.commands)
        assertEquals(listOf(0f to 0f, 100f to 0f, 100f to 60f, 0f to 60f), receiver.points)
    }

    @Test
    fun roundedPathStaysInsideItsBoundsAndTouchesEveryEdge() {
        val receiver = RecordingReceiver()
        ContinuousCurvature.emit(width = 100f, height = 60f, radius = 20f, receiver = receiver)

        assertEquals("M", receiver.commands.first())
        assertEquals("Z", receiver.commands.last())
        val xs = receiver.points.map { it.first }
        val ys = receiver.points.map { it.second }
        assertEquals(0f, xs.min(), 1e-3f)
        assertEquals(100f, xs.max(), 1e-3f)
        assertEquals(0f, ys.min(), 1e-3f)
        assertEquals(60f, ys.max(), 1e-3f)
    }

    @Test
    fun equalCornerRadiiMatchTheUniformOverload() {
        val uniform = RecordingReceiver()
        ContinuousCurvature.emit(100f, 60f, 20f, uniform)
        val perCorner = RecordingReceiver()
        ContinuousCurvature.emit(100f, 60f, 20f, 20f, 20f, 20f, perCorner)

        assertEquals(uniform.commands, perCorner.commands)
        assertEquals(uniform.points, perCorner.points)
    }
}
