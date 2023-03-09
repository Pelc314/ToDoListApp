package com.maciejpelcapps.todolistapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class ToDoEntry(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val data: String,
    var done: Boolean = false,
)
