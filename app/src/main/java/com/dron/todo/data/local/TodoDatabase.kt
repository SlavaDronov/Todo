package com.dron.todo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dron.todo.data.local.dao.TaskDao
import com.dron.todo.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}