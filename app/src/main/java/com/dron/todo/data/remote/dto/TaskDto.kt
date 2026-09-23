package com.dron.todo.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TaskDto(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("priority")
    val priority: Int = 1,

    @SerializedName("dueDate")
    val dueDate: Long = 0L,

    @SerializedName("completed")
    val completed: Boolean = false
)