package com.maciejpelcapps.todolistapp.data.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.maciejpelcapps.todolistapp.domain.ToDoEntry

@androidx.room.Dao
interface ToDoEntryDao {
    @Query("SELECT * FROM todos")
    fun getAllTodos(): List<ToDoEntry>

    @Insert
    fun saveTodo(todoEntry: ToDoEntry)

    @Delete
    fun deleteTodo(todoEntry: ToDoEntry)
}