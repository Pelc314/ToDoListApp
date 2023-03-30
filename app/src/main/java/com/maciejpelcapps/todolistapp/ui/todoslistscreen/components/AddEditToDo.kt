package com.maciejpelcapps.todolistapp.ui.todoslistscreen.components

import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.animateSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry
import com.maciejpelcapps.todolistapp.ui.todoslistscreen.AddNewTodoOffset
import com.maciejpelcapps.todolistapp.ui.todoslistscreen.AddNewTodoScale
import com.maciejpelcapps.todolistapp.ui.todoslistscreen.ToDoListViewModel
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
    changeAddingOrEditingTodoBoolean: (Boolean) -> Unit,
    changeWhichElement: (Int) -> Unit,
    modifier: Modifier = Modifier,
    changePromptSize: (AddNewTodoScale) -> Unit,
    changePromptOffset: (AddNewTodoOffset) -> Unit,
    changeTextValue: (String) -> Unit
) {
//    var addEditToDoSize by remember { mutableStateOf(AddNewTodoScale.Hidden) }
//    val addingNewToDoWindowTransition = updateTransition(
//        targetState = addEditToDoSize,
//        label = ""
//    )
//    val addingToDoWindowSizeAnim by addingNewToDoWindowTransition.animateSize(
//        transitionSpec = { tween(1000) },
//        label = ""
//    ) { scale ->
//        when (scale) {
//            AddNewTodoScale.Hidden -> Size(0.5f, 0.5f)
//            AddNewTodoScale.Normal -> Size(
//                LocalConfiguration.current.screenWidthDp.dp.value,
//                LocalConfiguration.current.screenHeightDp.dp.value
//            )
//        }
//    }
//
//    var addEditToDoOffset by remember {
//        mutableStateOf(AddNewTodoOffset.OffsetRight)
//    }
//    val addEditToDoOffsetTransition = updateTransition(targetState = addEditToDoOffset)
//    val addEditTodoOffsetAnim by addEditToDoOffsetTransition.animateOffset(
//        transitionSpec = { tween(1000) },
//        label = ""
//    ) { offset ->
//        when (offset) {
//            AddNewTodoOffset.OffsetRight -> Offset(
//                LocalConfiguration.current.screenWidthDp.dp.value / 2,
//                0f
//            )
//            AddNewTodoOffset.Normal -> Offset(
//                0f,
//                0f
//            )
//        }
//
//    }

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
                            delay(1000)
                            changeAddingOrEditingTodoBoolean(!addingOrEditingToDo)
                        }
                    }
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .height(300.dp)
                        .align(Alignment.Center)
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                        .clickable() { }
                ) {
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
                        )
                        IconButton(modifier = Modifier
                            .scale(1.5f)
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
                                            data = textOfPrompt
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
