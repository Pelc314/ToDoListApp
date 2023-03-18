package com.maciejpelcapps.todolistapp.data

import android.util.Log
import com.maciejpelcapps.todolistapp.data.database.ToDoEntryDao
import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry
import com.maciejpelcapps.todolistapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ToDoListRepositoryImpl @Inject constructor(
    private val toDoListDao: ToDoEntryDao,
) : ToDoListRepository {
    override fun getAllTodos(): Flow<Resource<List<ToDoEntry>>> {
        return flow {
            emit(Resource.Loading())
            Log.d("todos repository", "${toDoListDao.getAllTodos()}")
            toDoListDao.getAllTodos().collect {
                emit(
                    Resource.Success(
                        data = it
                    )
                )
            }
        }
    }

    override suspend fun saveTodo(todo: ToDoEntry) {
        toDoListDao.saveTodo(todo)
    }

    override suspend fun deleteTodo(todoEntry: ToDoEntry) {
        toDoListDao.deleteTodo(todoEntry)
    }
}