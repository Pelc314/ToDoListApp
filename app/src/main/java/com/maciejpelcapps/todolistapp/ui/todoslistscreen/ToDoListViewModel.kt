package com.maciejpelcapps.todolistapp.ui.todoslistscreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maciejpelcapps.todolistapp.data.ToDoListRepositoryImpl
import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry
import com.maciejpelcapps.todolistapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel @Inject constructor(
    private val repository: ToDoListRepositoryImpl,
) : ViewModel() {
    private var _toDosState = mutableStateOf(ToDoListState())
    val toDosState: State<ToDoListState> = _toDosState

    private val _noteColor = mutableStateOf<Int>(ToDoEntry.noteColors.get(5).toArgb())
    val noteColor: State<Int> = _noteColor

    init {
        getAllToDos()
    }

    fun getAllToDos() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllTodos().collect() { toDoList ->
                delay(500)
                when (toDoList) {
                    is Resource.Success -> {
                        Log.d("todos success", "${toDoList.data}")
                        _toDosState.value = ToDoListState(toDoList.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        Log.d("todos error", "${toDoList.message}")
                        _toDosState.value = ToDoListState(error = toDoList.message ?: "Error")
                    }
                    is Resource.Loading -> {
                        Log.d("todos loading", "${toDoList}")
                    }
                }
            }
        }
    }

    fun saveToDo(toDoEntry: ToDoEntry) {
        viewModelScope.launch() {
            val toDos = (_toDosState.value.toDosList) + toDoEntry
            _toDosState.value = _toDosState.value.copy(toDosList = toDos)
            repository.saveTodo(toDoEntry.copy(color = noteColor.value))
        }
    }

    fun editToDo(toDoEntry: ToDoEntry, index: Int) {
        viewModelScope.launch {
            _toDosState.value.toDosList[index].done = !_toDosState.value.toDosList[index].done
            _toDosState.value.toDosList[index].data = toDoEntry.data
            _toDosState.value = ToDoListState(toDosList = _toDosState.value.toDosList)
            //stuff above is used just to update screen, below updates the db
            repository.saveTodo(toDoEntry.copy(color = noteColor.value))
        }
    }

    fun deleteToDo(toDoEntry: ToDoEntry) {
        viewModelScope.launch {
            val toDos = (_toDosState.value.toDosList) - toDoEntry
            repository.deleteTodo(toDoEntry)
            _toDosState.value = ToDoListState(toDos)

        }
    }

    fun changeColor(colorInt: Int) {
        _noteColor.value = colorInt
    }
}