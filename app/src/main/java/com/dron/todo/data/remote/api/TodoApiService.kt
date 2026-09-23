package com.dron.todo.data.remote.api

import com.dron.todo.data.remote.dto.TaskDto
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoApiService {

    @GET("tasks")
    fun getTasks(): Single<List<TaskDto>>

    @GET("tasks/{id}")
    fun getTask(@Path("id") id: String): Single<TaskDto>

    @POST("tasks")
    fun createTask(@Body task: TaskDto): Single<TaskDto>

    @PUT("tasks/{id}")
    fun updateTask(
        @Path("id") id: String,
        @Body task: TaskDto
    ): Single<TaskDto>

    @DELETE("tasks/{id}")
    fun deleteTask(@Path("id") id: String): Completable
}