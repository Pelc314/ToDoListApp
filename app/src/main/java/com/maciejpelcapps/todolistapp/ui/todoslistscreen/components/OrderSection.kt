package com.maciejpelcapps.todolistapp.ui.todoslistscreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
            SortRadioButton(
                listOrder = listOrder,
                selectedOrder = ToDoListViewModel.TaskListOrder.ByColor,
                text = "Color",
                viewModel = viewModel,
                modifier = Modifier.align(CenterVertically)
            )
            SortRadioButton(
                listOrder = listOrder,
                selectedOrder = ToDoListViewModel.TaskListOrder.Alphabetical,
                text = "ABC",
                viewModel = viewModel,
                modifier = Modifier.align(CenterVertically)
            )
            SortRadioButton(
                listOrder = listOrder,
                selectedOrder = ToDoListViewModel.TaskListOrder.ByCompletion,
                text = "Done",
                viewModel = viewModel,
                modifier = Modifier.align(CenterVertically)
            )
        }
    }
}

