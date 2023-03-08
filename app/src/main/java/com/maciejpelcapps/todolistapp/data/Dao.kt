package com.maciejpelcapps.todolistapp.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.maciejpelcapps.todolistapp.domain.TodoEntry

@androidx.room.Dao
interface Dao {
    @Query("SELECT * FROM todos")
    fun getAllTodos():List<TodoEntry>
    @Insert
    fun saveTodo(todoEntry: TodoEntry)
    @Delete
    fun deleteTodo(todoEntry: TodoEntry)
}