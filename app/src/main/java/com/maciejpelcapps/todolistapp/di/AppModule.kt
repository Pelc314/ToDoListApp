package com.maciejpelcapps.todolistapp.di

import android.app.Application
import androidx.room.Room
import com.maciejpelcapps.todolistapp.data.ToDoListRepository
import com.maciejpelcapps.todolistapp.data.database.ToDoEntryDao
import com.maciejpelcapps.todolistapp.data.database.ToDoListDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDataBase(app: Application): ToDoListDatabase {
        return Room.databaseBuilder(
            app,
            ToDoListDatabase::class.java,
            "todo_list_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideToDoEntryDao(toDoListDatabase: ToDoListDatabase): ToDoEntryDao {
        return toDoListDatabase.toDoEntryDao()
    }
}