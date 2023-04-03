package com.maciejpelcapps.todolistapp.ui.todoslistscreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    taskOrder: Unit,
    onOrderChange: () -> Unit,
) {
    Column() {
        Text(text = "Sort")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "By Color")
            Text(text = "Alphabetically")
            Text(text = "By Completion")
        }
    }
}