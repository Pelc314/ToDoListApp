package com.maciejpelcapps.todolistapp.ui.todoslistscreen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.maciejpelcapps.todolistapp.ui.todoslistscreen.ToDoListViewModel

@Composable
fun SortRadioButton(
    listOrder: ToDoListViewModel.TaskListOrder,
    selectedOrder: ToDoListViewModel.TaskListOrder,
    viewModel: ToDoListViewModel,
    text: String,
    modifier: Modifier,
) {
    Box(
    ) {
        Row() {
            RadioButton(
                selected = listOrder == selectedOrder,
                modifier = modifier,
                onClick = {
                    // If user clicks selected radio button again it will return to default sorting
                    if (listOrder == selectedOrder) {
                        viewModel.sortEvent(ToDoListViewModel.TaskListOrder.Default)
                    } else {
                        viewModel.sortEvent(selectedOrder)
                    }
                },
            )
            Text(
                text = text,
                modifier = modifier,
            )
        }
    }
}