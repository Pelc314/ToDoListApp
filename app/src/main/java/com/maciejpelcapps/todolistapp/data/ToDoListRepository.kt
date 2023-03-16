package com.maciejpelcapps.todolistapp.data

import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry
import com.maciejpelcapps.todolistapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface ToDoListRepository {
   fun getAllTodos(): Flow<Resource<List<ToDoEntry>>>

    suspend fun saveTodo(todo: ToDoEntry)

    suspend fun deleteTodo(todoEntry: ToDoEntry)
}
