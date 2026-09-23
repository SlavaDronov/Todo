package com.dron.todo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val remoteId: Long? = null,

    val title: String,

    val description: String? = null,

    /** 0 = LOW, 1 = MEDIUM, 2 = HIGH */
    val priority: Int = 1,

    /** timestamp дедлайна */
    val dueDate: Long = 0L,

    /** timestamp создания — для сортировки */
    val createdAt: Long = System.currentTimeMillis(),

    val completed: Boolean = false
)