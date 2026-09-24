package com.dron.todo.domain.repository

import com.dron.todo.data.local.entity.TaskEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface TaskRepository {

    // ---------- Наблюдение за данными (Room) ----------

    fun observeAllSortedByCreatedAt(): Flowable<List<TaskEntity>>

    fun observeAllSortedByPriority(): Flowable<List<TaskEntity>>

    fun observeById(id: Long): Flowable<TaskEntity>

    fun search(query: String): Flowable<List<TaskEntity>>

    // ---------- Синхронизация с сервером ----------

    fun refreshFromNetwork(): Completable

    // ---------- Изменения (Room + сеть) ----------

    fun create(task: TaskEntity): Completable

    fun update(task: TaskEntity): Completable

    fun delete(task: TaskEntity): Completable

    fun toggleCompleted(id: Long, completed: Boolean): Completable
}