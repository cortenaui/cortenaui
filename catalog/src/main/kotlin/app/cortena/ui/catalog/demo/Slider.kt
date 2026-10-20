/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import framework.cortena.ui.components.Slider
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.Toggle

@Composable
fun SliderDemo() {
    var enable by remember { mutableStateOf(true) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Enable Slider")
        Toggle(checked = enable, onCheckedChange = { enable = it })
    }
    var sliderValue by remember { mutableFloatStateOf(0f) }
    Text("Slider")
    Column {
        Text("value int: ${sliderValue.toInt()}")
        Text("value float: $sliderValue")
    }
    Slider(
        value = { sliderValue },
        onValueChange = { sliderValue = it },
        valueRange = -4f..4f,
        enabled = enable,
    )
    var sliderDiscreteValue by remember { mutableFloatStateOf(0f) }
    Text("Slider Discrete")
    Text("value snapped: $sliderDiscreteValue")
    Slider(
        value = { sliderDiscreteValue },
        onValueChange = { sliderDiscreteValue = it },
        valueRange = -2f..2f,
        steps = 3, // Creates 4 intervals between -2 and 2, meaning a step size of 1f.
        enabled = enable,
    )
}
