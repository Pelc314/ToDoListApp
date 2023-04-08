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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maciejpelcapps.todolistapp.ui.todoslistscreen.components.AddEditTodo
import com.maciejpelcapps.todolistapp.ui.todoslistscreen.components.OrderSection
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
    var newTask by rememberSaveable {
        mutableStateOf(false)
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
                    newTask = true
                }
            }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add FAB")
            }
        }
    ) {
        Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Your tasks",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { viewModel.changeSortMenuVisibility() }) {
                    Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort")
                }
            }
            AnimatedVisibility(
                visible = viewModel.sortToggleExpanded.value,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    taskOrder = Unit,
                    onOrderChange = {
                        Unit
                    },
                    viewModel = viewModel
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier) {
                items(toDosListState.toDosList.size) { index ->
                    ToDoItem(
                        toDoEntry = toDosListState.toDosList[index],
                        viewModel = viewModel,
                        scope = scope,
                        scaffoldState = scaffoldState,
                        modifier = Modifier.clickable {
                            addingOrEditingToDo = true
                            textDataOfTask = toDosListState.toDosList[index].data
                            newTask = false
                            viewModel.apply {
                                changeColor(toDosListState.toDosList[index].color)
                                setCurrentItem(toDosListState.toDosList[index])
                            }
                            addEditToDoSize = AddNewTodoScale.Normal
                            addEditToDoOffset = AddNewTodoOffset.Normal
                        }
                    )
                }
            }
        }
        if (addingOrEditingToDo) {
            AddEditTodo(
                newTask = newTask,
                toDoEntry = viewModel.currentItem.value,
                addingOrEditingToDo = addingOrEditingToDo,
                scope = scope,
                scaffoldState = scaffoldState,
                textOfPrompt = textDataOfTask,
                viewModel = viewModel,
                changeAddingOrEditingTodoBoolean = { addingOrEditingToDo = it },
                changeToNewTask = { newTask = it },
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