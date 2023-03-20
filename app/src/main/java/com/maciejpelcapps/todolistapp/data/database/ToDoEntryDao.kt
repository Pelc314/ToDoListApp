package com.maciejpelcapps.todolistapp.data.database

import androidx.room.*
import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoEntryDao {
    @Query("SELECT * FROM todos")
    fun getAllTodos(): Flow<List<ToDoEntry>>

    @Upsert
    suspend fun saveTodo(todo: ToDoEntry)
    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getToDoById(id: Int): ToDoEntry?
    @Delete
    suspend fun deleteTodo(todoEntry: ToDoEntry)
}