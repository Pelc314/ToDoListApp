package com.maciejpelcapps.todolistapp.ui.todoslistscreen.components

import android.content.res.Resources.Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry
import com.maciejpelcapps.todolistapp.ui.todoslistscreen.ToDoListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ToDoItem(
    toDoEntry: ToDoEntry,
    viewModel: ToDoListViewModel,
    index: Int,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
) {
    var done by remember {
        mutableStateOf(toDoEntry.done)
    }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xC19DFFF5),
                        Color(0x88B0FCA0)
                    )
                ), shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth()
            .heightIn(40.dp)
    ) {
        Row() {
            RadioButton(
                selected = done,
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {
                    done = !done
                    viewModel.editToDo(
                        toDoEntry = toDoEntry,
                        index = index
                    )

                })
            if (done) {
                Text(
                    modifier = Modifier
                        .weight(1f).align(CenterVertically),
                    text = toDoEntry.data,
                    fontWeight = FontWeight.Light
                )
            } else {
                Text(
                    modifier = Modifier
                        .weight(1f).align(CenterVertically),
                    text = toDoEntry.data,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(modifier = Modifier.align(CenterVertically),onClick = {
                viewModel.deleteToDo(toDoEntry)
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(message = "Task deleted")
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete to do"
                )
            }
        }
    }
}