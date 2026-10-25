/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import framework.cortena.ui.components.ListItem
import framework.cortena.ui.components.ListView
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.components.Toggle
import framework.cortena.ui.theme.LocalColors

@Composable
fun ToggleDemo() {
    val colors = LocalColors.current
    var defaultChecked by remember { mutableStateOf(true) }
    var customChecked by remember { mutableStateOf(true) }
    ListView {
        item {
            ListItem(
                title = { Text("Default Toggle", role = TextRole.BodyMedium) },
                trailing = {
                    Toggle(checked = defaultChecked, onCheckedChange = { defaultChecked = it })
                },
            )
        }
        item {
            ListItem(
                title = { Text("Disabled Toggle", role = TextRole.BodyMedium) },
                trailing = {
                    Toggle(
                        checked = defaultChecked,
                        onCheckedChange = { defaultChecked = it },
                        enabled = false,
                    )
                },
            )
        }
        item {
            ListItem(
                title = { Text("Custom Color", role = TextRole.BodyMedium) },
                trailing = {
                    Toggle(
                        checked = customChecked,
                        onCheckedChange = { customChecked = it },
                        activeColor = Color(colors.primary),
                    )
                },
            )
        }
    }
}
