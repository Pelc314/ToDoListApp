package com.maciejpelcapps.todolistapp.ui.todoslistscreen

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maciejpelcapps.todolistapp.ui.todoslistscreen.components.ToDoItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ToDoListScreen(
    navController: NavController,
    viewModel: ToDoListViewModel = hiltViewModel(),
) {
    var text by remember {
        mutableStateOf("")
    }
    var addingNewToDo by remember {
        mutableStateOf(false)
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
            FloatingActionButton(onClick = { addingNewToDo = true }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add FAB")
            }
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(toDosListState.toDosList.size) { index ->
                        ToDoItem(
                            toDoEntry = toDosListState.toDosList[index],
                            viewModel = viewModel,
                            index = index,
                            scope = scope,
                            scaffoldState = scaffoldState,
                            modifier = Modifier
                        )
                    }
                }
            if (addingNewToDo) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f),
                ) {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = {
                            Text(text = "What you want to do?")
                        }, modifier = Modifier.weight(1f)
                    )
                    IconButton(modifier = Modifier
                        .padding(start = 20.dp)
                        .scale(1.5f), onClick = {
                        if (!text.isBlank()) {
                            viewModel.saveToDo(ToDoEntry(data = text))
                            text = ""
                            addingNewToDo = false
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

