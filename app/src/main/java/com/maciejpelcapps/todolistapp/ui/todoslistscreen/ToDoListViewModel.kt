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

    private var _sortToggleExpanded = mutableStateOf(false)
    val sortToggleExpanded: State<Boolean> = _sortToggleExpanded

    private val _taskColor = mutableStateOf<Int>(ToDoEntry.noteColors[0].toArgb())
    val taskColor: State<Int> = _taskColor

    private var _taskListOrder = mutableStateOf<TaskListOrder>(TaskListOrder.Default())
    val taskListOrder: State<TaskListOrder> = _taskListOrder

    init {
        sortEvent(taskListOrder.value)
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

    private fun getAllTodosAlphabetically() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllTodosAlphabetically().collect() { toDoList ->
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

    private fun getAllTodosByColor() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllTodosByColor().collect() { toDoList ->
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

    private fun getAllTodosByCompletion() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllTodosByCompletion().collect() { toDoList ->
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
            repository.saveTodo(toDoEntry.copy(color = taskColor.value))
            resetColorToDefault()
        }
    }

    fun editToDo(toDoEntry: ToDoEntry, index: Int) {
        viewModelScope.launch {
            _toDosState.value.toDosList[index].done = !_toDosState.value.toDosList[index].done
            _toDosState.value.toDosList[index].data = toDoEntry.data
            _toDosState.value = ToDoListState(toDosList = _toDosState.value.toDosList)
            //stuff above is used just to update screen, below updates the db
            repository.saveTodo(toDoEntry)
            resetColorToDefault()
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
        _taskColor.value = colorInt
    }

    fun resetColorToDefault() {
        _taskColor.value = ToDoEntry.noteColors[0].toArgb()
    }

    fun changeSortMenuVisibility() {
        _sortToggleExpanded.value = !_sortToggleExpanded.value

    }

    // This function invokes corresponding functions to edit task order
    fun sortEvent(listOrder: TaskListOrder) {
        when (listOrder) {
            is TaskListOrder.Alphabetical -> {
                getAllTodosAlphabetically()
                _taskListOrder.value = TaskListOrder.Alphabetical()
            }
            is TaskListOrder.ByColor -> {
                getAllTodosByColor()
                _taskListOrder.value = TaskListOrder.ByColor()
            }
            is TaskListOrder.ByCompletion -> {
                getAllTodosByCompletion()
                _taskListOrder.value = TaskListOrder.ByCompletion()
            }
            is TaskListOrder.Default -> {
                getAllToDos()
                _taskListOrder.value = TaskListOrder.Default()
            }
        }
    }

    sealed class TaskListOrder {
        class Alphabetical() : TaskListOrder()
        class ByColor() : TaskListOrder()
        class ByCompletion() : TaskListOrder()
        class Default() : TaskListOrder()
    }
}

