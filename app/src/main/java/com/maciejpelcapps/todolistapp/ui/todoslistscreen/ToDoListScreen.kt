package com.maciejpelcapps.todolistapp.ui.todoslistscreen

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.animateSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry
import com.maciejpelcapps.todolistapp.ui.todoslistscreen.components.ToDoItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ToDoListScreen(
    navController: NavController,
    viewModel: ToDoListViewModel = hiltViewModel(),
) {
    var text by rememberSaveable {
        mutableStateOf("")
    }
    var whichElement by rememberSaveable {
        mutableStateOf(-1)
    }
    var id by rememberSaveable {
        mutableStateOf(-1)
    }
    var addingOrEditingToDo by rememberSaveable {
        mutableStateOf(false)
    }

    var addEditToDoSize by remember { mutableStateOf(AddNewTodoScale.Hidden) }
    val addingNewToDoWindowTransition = updateTransition(
        targetState = addEditToDoSize,
        label = ""
    )
    val addingToDoWindowSizeAnim by addingNewToDoWindowTransition.animateSize(
        transitionSpec = { tween(1000) },
        label = ""
    ) { scale ->
        when (scale) {
            AddNewTodoScale.Hidden -> Size(0.5f, 0.5f)
            AddNewTodoScale.Normal -> Size(
                LocalConfiguration.current.screenWidthDp.dp.value,
                LocalConfiguration.current.screenHeightDp.dp.value
            )
        }
    }

    var addEditToDoOffset by remember {
        mutableStateOf(AddNewTodoOffset.OffsetRight)
    }
    val addEditToDoOffsetTransition = updateTransition(targetState = addEditToDoOffset)
    val addEditTodoOffsetAnim by addEditToDoOffsetTransition.animateOffset(
        transitionSpec = { tween(1000) },
        label = ""
    ) { offset ->
        when (offset) {
            AddNewTodoOffset.OffsetRight -> Offset(
                LocalConfiguration.current.screenWidthDp.dp.value / 2,
                0f
            )
            AddNewTodoOffset.Normal -> Offset(
                0f,
                0f
            )
        }

    }

    val toDosListState = viewModel.toDosState.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar {
                Text(
                    text = "To do list",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .align(CenterVertically)
                        .padding(8.dp)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                addingOrEditingToDo = true
                addEditToDoSize = AddNewTodoScale.Normal
                addEditToDoOffset = AddNewTodoOffset.Normal
            }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add FAB")
            }
        }
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            LazyColumn(modifier = Modifier) {
                items(toDosListState.toDosList.size) { index ->
                    ToDoItem(
                        toDoEntry = toDosListState.toDosList[index],
                        viewModel = viewModel,
                        index = index,
                        scope = scope,
                        scaffoldState = scaffoldState,
                        modifier = Modifier.clickable {
                            addingOrEditingToDo = true
                            text = toDosListState.toDosList[index].data
                            id = toDosListState.toDosList[index].id ?: -1
                            whichElement = index
                            addEditToDoSize = AddNewTodoScale.Normal
                            addEditToDoOffset = AddNewTodoOffset.Normal
                        }
                    )
                }
            }
        }
        if (addingOrEditingToDo) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .fillMaxSize()
                ) {
                    Box(
                        contentAlignment = Center,
                        modifier = Modifier
                            .size(
                                height = addingToDoWindowSizeAnim.height.dp,
                                width = addingToDoWindowSizeAnim.width.dp
                            )
                            .offset(
                                x = addEditTodoOffsetAnim.x.dp,
                                y = addEditTodoOffsetAnim.y.dp
                            )
                            .background(
                                Color.LightGray.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .align(CenterVertically)
                            .clickable {
                                scope.launch {
                                    addEditToDoSize = AddNewTodoScale.Hidden
                                    addEditToDoOffset = AddNewTodoOffset.OffsetRight
                                    delay(1000)
                                    addingOrEditingToDo = !addingOrEditingToDo
                                }
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .height(300.dp)
                                .align(Center)
                                .background(Color.White, shape = RoundedCornerShape(16.dp))
                                .clickable() { }
                        ) {
                            Column() {
                                TextField(
                                    value = text,
                                    onValueChange = { text = it },
                                    placeholder = {
                                        Text(text = "What do you want to do?")
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp, horizontal = 8.dp)
                                        .align(CenterHorizontally)
                                        .weight(1f)
                                )
                                IconButton(modifier = Modifier
                                    .scale(1.5f)
                                    .align(CenterHorizontally), onClick = {
                                    if (!text.isBlank()) {
                                        if (whichElement == -1) {
                                            viewModel.saveToDo(ToDoEntry(data = text))
                                            scope.launch {
                                                scaffoldState.snackbarHostState.showSnackbar(
                                                    message = "Task saved"
                                                )
                                            }
                                        } else {
                                            viewModel.editToDo(
                                                toDoEntry = ToDoEntry(
                                                    id = id,
                                                    data = text
                                                ), index = whichElement
                                            )
                                            scope.launch {
                                                scaffoldState.snackbarHostState.showSnackbar(
                                                    message = "Task edited"
                                                )
                                            }
                                        }
                                        whichElement = -1
                                        text = ""
                                        scope.launch {
                                            addEditToDoSize = AddNewTodoScale.Hidden
                                            addEditToDoOffset = AddNewTodoOffset.OffsetRight
                                            delay(1000)
                                            addingOrEditingToDo = false
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
    }
}

private enum class AddNewTodoScale {
    Hidden,
    Normal
}

private enum class AddNewTodoOffset {
    OffsetRight,
    Normal
}