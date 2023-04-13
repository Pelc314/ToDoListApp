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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry
import com.maciejpelcapps.todolistapp.ui.todoslistscreen.ToDoListViewModel
import com.maciejpelcapps.todolistapp.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ToDoItem(
    toDoEntry: ToDoEntry,
    viewModel: ToDoListViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier,
) {
    var done = toDoEntry.done
    val animTime = Constants.ANIM_TIME
    var itemLookState by remember { mutableStateOf(ToDoItemLookState.OffsetRight) }
    val transition = updateTransition(targetState = itemLookState)
    val offset by transition.animateOffset(
        transitionSpec = { tween(animTime) },
        label = ""
    ) { offset ->
        when (offset) {
            ToDoItemLookState.OffsetRight -> Offset(
                LocalConfiguration.current.screenWidthDp.dp.value,
                0f
            )
            ToDoItemLookState.Normal -> Offset(0f, 0f)
            ToDoItemLookState.OffsetLeft -> Offset(
                -LocalConfiguration.current.screenWidthDp.dp.value,
                0f
            )
            ToDoItemLookState.OffsetDown -> Offset(x = 0f, y = 60.dp.value)
            ToDoItemLookState.OffsetLeftUp -> Offset(
                -LocalConfiguration.current.screenWidthDp.dp.value,
                -60.dp.value
            )
        }
    }

    val customAlpha by rememberSaveable() {
        mutableStateOf(1f)
    }

    LaunchedEffect(scope) {
        itemLookState = ToDoItemLookState.Normal
    }

    var color by rememberSaveable() {
        mutableStateOf(toDoEntry.color)
    }

    if (toDoEntry.color == -1) {
        color = ToDoEntry.noteColors[0].toArgb()
    } else {
        color = toDoEntry.color
    }

    Box(
        modifier = modifier
            .padding(8.dp)
            .offset(offset.x.dp, offset.y.dp)
            .background(
                color = Color(color), shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth()
            .heightIn(40.dp)
            .alpha(customAlpha)
    ) {
        Row() {
            RadioButton(
                selected = done,
                modifier = modifier.align(CenterVertically),
                onClick = {
                    done = !done
                    viewModel.saveToDo(
                        toDoEntry = toDoEntry.copy(
                            done = done
                        ),
                        changeColor = false
                    )
                })
            if (done) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .align(CenterVertically),
                    text = toDoEntry.data,
                    fontWeight = FontWeight.Light,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )
            } else {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .align(CenterVertically),
                    text = toDoEntry.data,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )
            }
            IconButton(modifier = Modifier.align(CenterVertically), onClick = {
                scope.launch {
                    viewModel.deleteToDo(toDoEntry)
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = "Task deleted",
                        duration = SnackbarDuration.Short,
                        actionLabel = "Undo"
                    )
                    when (snackbarResult) {
                        SnackbarResult.ActionPerformed -> viewModel.saveToDo(toDoEntry, false)
                        SnackbarResult.Dismissed -> Unit
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete to do",
                    tint = Color.Black
                )
            }
        }
    }
}

private enum class ToDoItemLookState {
    OffsetRight,
    Normal,
    OffsetLeft,
    OffsetDown,
    OffsetLeftUp
}