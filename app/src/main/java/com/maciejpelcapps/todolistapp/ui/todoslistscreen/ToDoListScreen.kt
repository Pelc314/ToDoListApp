package com.maciejpelcapps.todolistapp.ui.todoslistscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry
import androidx.compose.material.icons.filled.Save
import androidx.compose.ui.unit.dp

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

    Column(modifier = Modifier.padding(16.dp)) {
        LazyColumn() {
            items(toDosListState.toDosList.size) { index ->
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = toDosListState.toDosList[index].data
                )
            }
        }
        Row(
            modifier = Modifier.padding(16.dp),
            ) {
            if (addingNewToDo) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = {
                        Text(text = "What you want to do?")
                    }
                )
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = "Save button",
                    modifier = Modifier.clickable {
                        viewModel.saveToDo(ToDoEntry(data = text))
                        text = ""
                        addingNewToDo = false
                        viewModel.getAllToDos()
                    }
                )
            }
        }
        Button(onClick = { addingNewToDo = true }, content = { Text(text = "Dodaj") })
    }
}

