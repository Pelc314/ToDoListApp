package com.maciejpelcapps.todolistapp.ui.todoslistscreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maciejpelcapps.todolistapp.data.ToDoListRepository
import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry
import com.maciejpelcapps.todolistapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel @Inject constructor(
    private val repository: ToDoListRepository,
) : ViewModel() {
    private var _toDosState = mutableStateOf(ToDoListState())
    val toDosState: State<ToDoListState> = _toDosState

    init {
        getAllToDos()
    }

    fun getAllToDos() {
        viewModelScope.launch {
            repository.getAllTodos().collect() { toDoList ->
                when (toDoList) {
                    is Resource.Success -> {
                        _toDosState.value = ToDoListState(toDoList.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        _toDosState.value = ToDoListState(error = toDoList.message ?: "Error")
                    }
                    is Resource.Loading -> {
                        Unit
                    }
                }
            }
        }
    }

    fun saveToDo(toDoEntry: ToDoEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            val toDos = (_toDosState.value.toDosList) + toDoEntry
            _toDosState.value = ToDoListState(toDosList = toDos)
            repository.saveTodo(toDos)
        }
    }
}