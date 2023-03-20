package com.maciejpelcapps.todolistapp.ui.todoslistscreen.components

import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    modifier: Modifier = Modifier
) {
    var done by remember {
        mutableStateOf(toDoEntry.done)
    }
    var itemLookState by remember{ mutableStateOf(ToDoItemLookState.Offset) }
    val transition = updateTransition(targetState = itemLookState)
    val offset by transition.animateOffset(transitionSpec = { tween(1000) }) { offset ->
        when(offset){
        ToDoItemLookState.Offset -> Offset(200f, 0f)
        ToDoItemLookState.Normal->Offset(0f, 0f)
        }
    }
    LaunchedEffect(itemLookState){
        itemLookState = ToDoItemLookState.Normal
    }
    Box(
        modifier = Modifier
            .padding(8.dp)
            .offset(offset.x.dp, offset.y.dp)
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
                modifier = modifier.align(CenterVertically),
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
                        .weight(1f)
                        .align(CenterVertically),
                    text = toDoEntry.data,
                    fontWeight = FontWeight.Light
                )
            } else {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .align(CenterVertically),
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

private enum class ToDoItemLookState {
    Offset,
    Normal
}