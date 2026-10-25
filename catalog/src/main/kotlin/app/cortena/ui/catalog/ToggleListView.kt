/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog

import androidx.compose.runtime.Composable
import framework.cortena.ui.components.ListItem
import framework.cortena.ui.components.ListView
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.components.Toggle

/** A [ListView] with a single row: [title] on the start and a [Toggle] on the end. */
@Composable
fun ToggleListView(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    ListView {
        item {
            ListItem(
                title = { Text(title, role = TextRole.BodyMedium) },
                trailing = { Toggle(checked = checked, onCheckedChange = onCheckedChange) },
            )
        }
    }
}
