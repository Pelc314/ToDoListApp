package com.maciejpelcapps.todolistapp.ui.todoslistscreen.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry
import com.maciejpelcapps.todolistapp.ui.todoslistscreen.AddNewTodoOffset
import com.maciejpelcapps.todolistapp.ui.todoslistscreen.AddNewTodoScale
import com.maciejpelcapps.todolistapp.ui.todoslistscreen.ToDoListViewModel
import com.maciejpelcapps.todolistapp.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun AddEditTodo(
    whichElement: Int,
    addingOrEditingToDo: Boolean,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    textOfPrompt: String,
    viewModel: ToDoListViewModel,
    id: Int,
    passedTaskColor: Int,
    changeAddingOrEditingTodoBoolean: (Boolean) -> Unit,
    changeWhichElement: (Int) -> Unit,
    changePromptSize: (AddNewTodoScale) -> Unit,
    changePromptOffset: (AddNewTodoOffset) -> Unit,
    changeTextValue: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val taskBackgroundAnimatable = remember {
        Animatable(
            Color(
                viewModel.taskColor.value
            ),
        )
    }

    LaunchedEffect(scope) {

    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .background(
                        Color.LightGray.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .align(Alignment.CenterVertically)
                    .clickable {
                        scope.launch {
                            changePromptSize(AddNewTodoScale.Hidden)
                            changePromptOffset(AddNewTodoOffset.OffsetRight)
                            delay(Constants.ANIM_TIME.toLong())
                            changeTextValue("")
                            changeWhichElement(-1)
                            viewModel.resetColorToDefault()
                            changeAddingOrEditingTodoBoolean(!addingOrEditingToDo)
                        }
                    }
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .heightIn(min = 100.dp, max = 350.dp)
                        .align(Alignment.Center)
                        .background(
                            (taskBackgroundAnimatable.value),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable() {

                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ToDoEntry.noteColors.forEach { color ->
                            val colorInt = color.toArgb()
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .shadow(15.dp, CircleShape)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = 3.dp,
                                        color = if (viewModel.taskColor.value == colorInt) {
                                            Color.Black
                                        } else {
                                            Color.Transparent
                                        },
                                        shape = CircleShape,
                                    )
                                    .clickable {
                                        scope.launch {
                                            taskBackgroundAnimatable.animateTo(
                                                targetValue = Color(colorInt),
                                                animationSpec = tween(durationMillis = Constants.ANIM_TIME),
                                            )
                                        }
                                        viewModel.changeColor(colorInt)
                                    },
                            )
                        }
                    }
                    Column() {
                        TextField(
                            value = textOfPrompt,
                            onValueChange = changeTextValue,
                            placeholder = {
                                Text(text = "What do you want to do?")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp, horizontal = 8.dp)
                                .align(Alignment.CenterHorizontally)
                                .weight(1f)
                                .background(Color.White)
                        )
                        IconButton(modifier = Modifier
                            .scale(1.7f)
                            .padding(bottom = 8.dp)
                            .align(Alignment.CenterHorizontally), onClick = {
                            if (!textOfPrompt.isBlank()) {
                                if (whichElement == -1) {
                                    viewModel.saveToDo(ToDoEntry(data = textOfPrompt))
                                    scope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            message = "Task saved"
                                        )
                                    }
                                } else {
                                    viewModel.editToDo(
                                        toDoEntry = ToDoEntry(
                                            id = id,
                                            data = textOfPrompt,
                                            color = viewModel.taskColor.value
                                        ), index = whichElement
                                    )
                                    scope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            message = "Task edited"
                                        )
                                    }
                                }
                                changeWhichElement(-1)
                                changeTextValue("")
                                viewModel.resetColorToDefault()
                                scope.launch {
                                    changePromptSize(AddNewTodoScale.Hidden)
                                    changePromptOffset(AddNewTodoOffset.OffsetRight)
                                    delay(1000)
                                    changeAddingOrEditingTodoBoolean(false)
                                }
                            } else {
                                scope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(message = "Task cannot be empty")
                                }
                            }

                        }) {
                            Icon(
                                imageVector = Icons.Filled.Save,
                                contentDescription = "Save button",
                            )
                        }
                    }
                }
            }
        }
    }
}
