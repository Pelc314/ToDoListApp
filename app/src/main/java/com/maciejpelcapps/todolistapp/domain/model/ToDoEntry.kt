package com.maciejpelcapps.todolistapp.domain.model

import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maciejpelcapps.todolistapp.ui.theme.*


@Entity(tableName = "todos")
data class ToDoEntry(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var data: String,
    var color: Int = RedPink.toArgb(),
    var done: Boolean = false,
){
    companion object {
        val noteColors = listOf(lightGreen, RedOrange, Violet, BabyBlue, RedPink)
    }
}
