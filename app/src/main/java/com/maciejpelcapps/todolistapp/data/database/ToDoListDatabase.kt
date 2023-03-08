package com.maciejpelcapps.todolistapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry

@Database(entities = [ToDoEntry::class], version = 1)
abstract class ToDoListDatabase:RoomDatabase() {
    abstract fun toDoEntryDao(): ToDoEntryDao
}