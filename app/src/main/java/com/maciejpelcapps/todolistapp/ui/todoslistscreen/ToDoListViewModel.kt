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
import kotlinx.coroutines.flow.cancellable
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

    private var _taskListOrder = mutableStateOf<TaskListOrder>(TaskListOrder.Default)
    val taskListOrder: State<TaskListOrder> = _taskListOrder

    private var getDataFromDbJob = viewModelScope.launch {}

    private var _currentItem = mutableStateOf<ToDoEntry>(ToDoEntry(1, ""))
    val currentItem: State<ToDoEntry> = _currentItem

    init {
        sortEvent(taskListOrder.value)
    }

    private fun getAllToDos() {
        getDataFromDbJob = viewModelScope.launch(Dispatchers.IO) {
            repository.getAllTodos().cancellable().collect() { toDoList ->
                delay(500)
                when (toDoList) {
                    is Resource.Success -> {
                        Log.d("todos success", "${toDoList.data}")
                        _toDosState.value =
                            ToDoListState(toDoList.data ?: emptyList(), isLoading = false)
                    }
                    is Resource.Error -> {
                        Log.d("todos error", "${toDoList.message}")
                        _toDosState.value =
                            ToDoListState(error = toDoList.message ?: "Error", isLoading = false)
                    }
                    is Resource.Loading -> {
                        _toDosState.value = ToDoListState(isLoading = true)
                        Log.d("todos loading", "${toDoList}")
                    }
                }
            }
        }
    }

    private fun getAllTodosAlphabetically() {
        getDataFromDbJob = viewModelScope.launch(Dispatchers.IO) {
            repository.getAllTodosAlphabetically().cancellable().collect() { toDoList ->
                delay(500)
                when (toDoList) {
                    is Resource.Success -> {
                        Log.d("todos success", "${toDoList.data}")
                        _toDosState.value =
                            ToDoListState(toDoList.data ?: emptyList(), isLoading = false)
                    }
                    is Resource.Error -> {
                        Log.d("todos error", "${toDoList.message}")
                        _toDosState.value =
                            ToDoListState(error = toDoList.message ?: "Error", isLoading = false)
                    }
                    is Resource.Loading -> {
                        _toDosState.value = ToDoListState(isLoading = true)
                        Log.d("todos loading", "${toDoList}")
                    }
                }
            }
        }
    }

    private fun getAllTodosByColor() {
        getDataFromDbJob = viewModelScope.launch(Dispatchers.IO) {
            repository.getAllTodosByColor().cancellable().collect() { toDoList ->
                delay(500)
                when (toDoList) {
                    is Resource.Success -> {
                        Log.d("todos success", "${toDoList.data}")
                        _toDosState.value =
                            ToDoListState(toDoList.data ?: emptyList(), isLoading = false)
                    }
                    is Resource.Error -> {
                        Log.d("todos error", "${toDoList.message}")
                        _toDosState.value =
                            ToDoListState(error = toDoList.message ?: "Error", isLoading = false)
                    }
                    is Resource.Loading -> {
                        _toDosState.value = ToDoListState(isLoading = true)
                        Log.d("todos loading", "${toDoList}")
                    }
                }
            }
        }
    }

    private fun getAllTodosByCompletion() {
        getDataFromDbJob = viewModelScope.launch(Dispatchers.IO) {
            repository.getAllTodosByCompletion().cancellable().collect() { toDoList ->
                delay(500)
                when (toDoList) {
                    is Resource.Success -> {
                        Log.d("todos success", "${toDoList.data}")
                        _toDosState.value =
                            ToDoListState(toDoList.data ?: emptyList(), isLoading = false)
                    }
                    is Resource.Error -> {
                        Log.d("todos error", "${toDoList.message}")
                        _toDosState.value =
                            ToDoListState(error = toDoList.message ?: "Error", isLoading = false)
                    }
                    is Resource.Loading -> {
                        _toDosState.value = ToDoListState(isLoading = true)
                        Log.d("todos loading", "${toDoList}")
                    }
                }
            }
        }
    }

    fun saveToDo(toDoEntry: ToDoEntry, changeColor: Boolean = true) {
        viewModelScope.launch() {
            if (!changeColor) changeColor(toDoEntry.color)
            repository.saveTodo(toDoEntry.copy(color = taskColor.value))
            resetColorToDefault()
        }
    }

    fun deleteToDo(toDoEntry: ToDoEntry) {
        viewModelScope.launch {
            repository.deleteTodo(toDoEntry)
        }
    }

    fun setCurrentItem(currentItem: ToDoEntry) { // sets item which is being modified
        _currentItem.value = currentItem
    }

    fun changeColor(colorInt: Int) {
        _taskColor.value = colorInt
    }

    fun resetColorToDefault() { // resets color after editing or creating an item is completed
        _taskColor.value = ToDoEntry.noteColors[0].toArgb()
    }

    fun changeSortMenuVisibility() {
        _sortToggleExpanded.value = !_sortToggleExpanded.value

    }

    // This function invokes corresponding functions to edit task order
    fun sortEvent(listOrder: TaskListOrder) {
        getDataFromDbJob.cancel() // this cancels the job so we don't receive many different flows from the db only the one which is interesting to us
        when (listOrder) {
            is TaskListOrder.Alphabetical -> {
                getAllTodosAlphabetically()
                _taskListOrder.value = TaskListOrder.Alphabetical
            }
            is TaskListOrder.ByColor -> {
                getAllTodosByColor()
                _taskListOrder.value = TaskListOrder.ByColor
            }
            is TaskListOrder.ByCompletion -> {
                getAllTodosByCompletion()
                _taskListOrder.value = TaskListOrder.ByCompletion
            }
            is TaskListOrder.Default -> {
                getAllToDos()
                _taskListOrder.value = TaskListOrder.Default
            }
        }
    }

    sealed class TaskListOrder {
        object Alphabetical : TaskListOrder()
        object ByColor : TaskListOrder()
        object ByCompletion : TaskListOrder()
        object Default : TaskListOrder()
    }
}

