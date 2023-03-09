package com.maciejpelcapps.todolistapp.ui.todoslistscreen

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.outlined.Add
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

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
                    modifier = Modifier.align(CenterVertically)
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
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .background(color = Color.LightGray, shape = RoundedCornerShape(16.dp))
                            .fillMaxWidth()
                            .heightIn(40.dp)
                    ) {
                        Row() {
                            var done by remember {
                                mutableStateOf(
                                    toDosListState.toDosList[index].done

                                )
                            }
                            RadioButton(
                                selected = done,
                                modifier = Modifier.align(CenterVertically),
                                onClick = {
                                    done = !done
                                    viewModel.editToDo(
                                        toDoEntry = toDosListState.toDosList[index],
                                        index = index
                                    )

                                })
                            Text(
                                modifier = Modifier
                                    .padding(start = 8.dp, top = 11.dp)
                                    .weight(1f),
                                text = toDosListState.toDosList[index].data
                            )
                            IconButton(onClick = {
                                viewModel.deleteToDo(toDosListState.toDosList[index])
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
                        }
                    )
                    IconButton(onClick = {
                        viewModel.saveToDo(ToDoEntry(data = text))
                        text = ""
                        addingNewToDo = false
                        viewModel.getAllToDos()
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

