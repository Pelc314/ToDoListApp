package com.maciejpelcapps.todolistapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class ToDoEntryEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null
)
