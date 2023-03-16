package com.maciejpelcapps.todolistapp.data.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry
import kotlinx.coroutines.flow.Flow

@androidx.room.Dao
interface ToDoEntryDao {
    @Query("SELECT * FROM todos")
    fun getAllTodos(): List<ToDoEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTodo(todo: ToDoEntry)

    @Delete
    suspend fun deleteTodo(todoEntry: ToDoEntry)
}