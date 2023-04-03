package com.maciejpelcapps.todolistapp.ui.todoslistscreen

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.animateSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maciejpelcapps.todolistapp.ui.todoslistscreen.components.AddEditTodo
import com.maciejpelcapps.todolistapp.ui.todoslistscreen.components.ToDoItem
import com.maciejpelcapps.todolistapp.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ToDoListScreen(
    navController: NavController,
    viewModel: ToDoListViewModel = hiltViewModel(),
) {
    var textDataOfTask by rememberSaveable {
        mutableStateOf("")
    }
    var whichElement by rememberSaveable {
        mutableStateOf(-1)
    }
    var id by rememberSaveable {
        mutableStateOf(-1)
    }
    val taskColor by rememberSaveable {
        mutableStateOf(viewModel.taskColor.value)
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
        transitionSpec = { tween(Constants.ANIM_TIME) },
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
        transitionSpec = { tween(Constants.ANIM_TIME) },
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
                scope.launch {
                    if (addingOrEditingToDo) {
                        addEditToDoSize = AddNewTodoScale.Hidden
                        addEditToDoOffset = AddNewTodoOffset.OffsetRight
                        delay(Constants.ANIM_TIME.toLong())
                        addingOrEditingToDo = false
                        delay(100)
                    }
                    addingOrEditingToDo = true
                    addEditToDoSize = AddNewTodoScale.Normal
                    addEditToDoOffset = AddNewTodoOffset.Normal
                    viewModel.resetColorToDefault()
                    textDataOfTask = ""
                    whichElement = -1
                }
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
                            textDataOfTask = toDosListState.toDosList[index].data
                            id = toDosListState.toDosList[index].id ?: -1
                            whichElement = index
                            viewModel.changeColor(toDosListState.toDosList[index].color)
                            addEditToDoSize = AddNewTodoScale.Normal
                            addEditToDoOffset = AddNewTodoOffset.Normal
                        }
                    )
                }
            }
        }
        if (addingOrEditingToDo) {
            AddEditTodo(
                whichElement = whichElement,
                addingOrEditingToDo = addingOrEditingToDo,
                scope = scope,
                scaffoldState = scaffoldState,
                textOfPrompt = textDataOfTask,
                viewModel = viewModel,
                id = id,
                passedTaskColor = taskColor,
                changeAddingOrEditingTodoBoolean = { addingOrEditingToDo = it },
                changeWhichElement = { whichElement = it },
                changePromptSize = { addEditToDoSize = it },
                changePromptOffset = { addEditToDoOffset = it },
                changeTextValue = { textDataOfTask = it },
                modifier = Modifier
                    .size(
                        height = addingToDoWindowSizeAnim.height.dp,
                        width = addingToDoWindowSizeAnim.width.dp
                    )
                    .offset(
                        x = addEditTodoOffsetAnim.x.dp,
                        y = addEditTodoOffsetAnim.y.dp
                    )
            )
        }
    }
}

enum class AddNewTodoScale {
    Hidden,
    Normal
}

enum class AddNewTodoOffset {
    OffsetRight,
    Normal
}