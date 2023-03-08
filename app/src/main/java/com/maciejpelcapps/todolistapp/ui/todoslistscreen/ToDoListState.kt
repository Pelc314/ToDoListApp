package com.maciejpelcapps.todolistapp.ui.todoslistscreen

import com.maciejpelcapps.todolistapp.domain.model.ToDoEntry

data class ToDoListState(
    val toDosList: List<ToDoEntry> = emptyList(),
    val error:String = ""
)
