package com.maciejpelcapps.todolistapp.ui.todoslistscreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import com.maciejpelcapps.todolistapp.ui.todoslistscreen.ToDoListViewModel

@Composable
fun OrderSection(
    viewModel: ToDoListViewModel,
) {
    val listOrder = viewModel.taskListOrder.value
    Column() {
        Text(text = "Sort By")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            RadioButton(
                selected = listOrder is ToDoListViewModel.TaskListOrder.ByColor,
                modifier = Modifier.align(CenterVertically),
                onClick = {
                    // If user clicks selected radio button again it will return to default sorting
                    if (listOrder is ToDoListViewModel.TaskListOrder.ByColor) {
                        viewModel.sortEvent(ToDoListViewModel.TaskListOrder.Default())
                    } else {
                        viewModel.sortEvent(ToDoListViewModel.TaskListOrder.ByColor())
                    }
                },
            )
            Text(
                text = "Color",
                modifier = Modifier.align(CenterVertically),
                maxLines = 1
            )
            RadioButton(
                selected = listOrder is ToDoListViewModel.TaskListOrder.Alphabetical,
                modifier = Modifier.align(CenterVertically),
                onClick = {
                    if (listOrder is ToDoListViewModel.TaskListOrder.Alphabetical) {
                        viewModel.sortEvent(ToDoListViewModel.TaskListOrder.Default())
                    } else {
                        viewModel.sortEvent(ToDoListViewModel.TaskListOrder.Alphabetical())
                    }
                },
            )
            Text(
                text = "ABC",
                modifier = Modifier.align(CenterVertically),
                maxLines = 1
            )
            RadioButton(
                selected = listOrder is ToDoListViewModel.TaskListOrder.ByCompletion,
                modifier = Modifier.align(CenterVertically),
                onClick = {
                    if (listOrder is ToDoListViewModel.TaskListOrder.ByCompletion) {
                        viewModel.sortEvent(ToDoListViewModel.TaskListOrder.Default())
                    } else {
                        viewModel.sortEvent(ToDoListViewModel.TaskListOrder.ByCompletion())
                    }
                },
            )
            Text(
                text = "Done",
                modifier = Modifier.align(CenterVertically),
                maxLines = 1
            )
        }
    }
}

