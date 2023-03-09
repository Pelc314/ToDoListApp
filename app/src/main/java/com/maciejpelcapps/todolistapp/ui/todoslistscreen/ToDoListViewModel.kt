package com.maciejpelcapps.todolistapp.ui.todoslistscreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maciejpelcapps.todolistapp.data.ToDoListRepository
import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry
import com.maciejpelcapps.todolistapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
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
        Log.d("todos init","lol")
    }

    fun getAllToDos() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllTodos().collect() { toDoList ->
                when (toDoList) {
                    is Resource.Success -> {
                        Log.d("todos success","${toDoList.data}")
                        delay(100)
                        val toDoEntries = toDoList.data?: emptyList()
                        _toDosState.value = ToDoListState(toDoEntries)
                    }
                    is Resource.Error -> {
                        Log.d("todos error","${toDoList.message}")
                        _toDosState.value = ToDoListState(error = toDoList.message ?: "Error")
                    }
                    is Resource.Loading -> {
                        Log.d("todos loading","${toDoList}")
                    }
                }
            }
        }
    }

    fun saveToDo(toDoEntry: ToDoEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            val toDos = (_toDosState.value.toDosList) + toDoEntry
            _toDosState.value = ToDoListState(toDosList = toDos)
            repository.saveTodo(toDoEntry)
        }
    }
}