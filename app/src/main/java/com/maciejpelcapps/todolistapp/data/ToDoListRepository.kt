package com.maciejpelcapps.todolistapp.data

import androidx.lifecycle.viewmodel.compose.viewModel
import com.maciejpelcapps.todolistapp.data.database.ToDoEntryDao
import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry
import com.maciejpelcapps.todolistapp.util.Resource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ToDoListRepository @Inject constructor(
    private val toDoListDao: ToDoEntryDao,
) {
    suspend fun getAllTodos(): Flow<Resource<List<ToDoEntry>>> {
        return flow {
            try {
                Resource.Success(data = toDoListDao.getAllTodos())
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "unknownError")
            }
        }
    }

    suspend fun saveTodo(toDoEntry: ToDoEntry) {
        toDoListDao.saveTodo(toDoEntry)
    }

    suspend fun deleteTodo(toDoEntry: ToDoEntry) {
        toDoListDao.deleteTodo(toDoEntry)
    }
}