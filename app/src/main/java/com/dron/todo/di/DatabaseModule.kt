package com.dron.todo.di

import android.content.Context
import androidx.room.Room
import com.dron.todo.data.local.TodoDatabase
import com.dron.todo.data.local.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TodoDatabase {
        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "todo.db"
        )
            .fallbackToDestructiveMigration() // для разработки; в проде — миграции
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(db: TodoDatabase): TaskDao = db.taskDao()
}