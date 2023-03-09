package com.maciejpelcapps.todolistapp.data

import android.util.Log
import com.maciejpelcapps.todolistapp.data.database.ToDoEntryDao
import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry
import com.maciejpelcapps.todolistapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ToDoListRepository @Inject constructor(
    private val toDoListDao: ToDoEntryDao,
) {
    suspend fun getAllTodos(): Flow<Resource<List<ToDoEntry>>> {
        return flow {
                Log.d("todos repository", "${toDoListDao.getAllTodos()}")
                emit(Resource.Success(data = toDoListDao.getAllTodos()))
        }
    }


    suspend fun saveTodo(toDo: ToDoEntry) {
        toDoListDao.saveTodo(toDo)
    }

    suspend fun deleteTodo(toDoEntry: ToDoEntry) {
        toDoListDao.deleteTodo(toDoEntry)
    }
}