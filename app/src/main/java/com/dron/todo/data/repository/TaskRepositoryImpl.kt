package com.dron.todo.data.repository

import com.dron.todo.data.local.dao.TaskDao
import com.dron.todo.data.local.entity.TaskEntity
import com.dron.todo.data.remote.api.TodoApiService
import com.dron.todo.data.remote.mapper.toDto
import com.dron.todo.data.remote.mapper.toEntities
import com.dron.todo.data.remote.mapper.toEntity
import com.dron.todo.domain.repository.TaskRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val apiService: TodoApiService
) : TaskRepository {

    // ---------- Наблюдение (только Room) ----------

    override fun observeAllSortedByCreatedAt(): Flowable<List<TaskEntity>> =
        taskDao.observeAllSortedByCreatedAt()

    override fun observeAllSortedByPriority(): Flowable<List<TaskEntity>> =
        taskDao.observeAllSortedByPriority()

    override fun observeById(id: Long): Flowable<TaskEntity> =
        taskDao.observeById(id)

    override fun search(query: String): Flowable<List<TaskEntity>> =
        taskDao.search(query)

    // ---------- Синхронизация с сервером ----------

    override fun refreshFromNetwork(): Completable =
        apiService.getTasks()
            .subscribeOn(Schedulers.io())
            .flatMapCompletable { dtos ->
                taskDao.insertAll(dtos.toEntities())
            }

    // ---------- Изменения (Room + сеть) ----------

    override fun create(task: TaskEntity): Completable =
        taskDao.insert(task)
            .subscribeOn(Schedulers.io())
            .flatMapCompletable { localId ->
                val taskWithId = task.copy(id = localId)
                apiService.createTask(taskWithId.toDto())
                    .flatMapCompletable { dto ->
                        // Обновляем remoteId после ответа сервера
                        taskDao.update(
                            taskWithId.copy(remoteId = dto.id?.toLongOrNull())
                        )
                    }
                    .onErrorComplete() // если сеть упала — данные уже в Room
            }

    override fun update(task: TaskEntity): Completable =
        taskDao.update(task)
            .subscribeOn(Schedulers.io())
            .andThen(
                task.remoteId?.let { remoteId ->
                    apiService.updateTask(remoteId.toString(), task.toDto())
                        .ignoreElement()
                        .onErrorComplete()
                } ?: Completable.complete()
            )

    override fun delete(task: TaskEntity): Completable =
        taskDao.delete(task)
            .subscribeOn(Schedulers.io())
            .andThen(
                task.remoteId?.let { remoteId ->
                    apiService.deleteTask(remoteId.toString())
                        .onErrorComplete()
                } ?: Completable.complete()
            )

    override fun toggleCompleted(id: Long, completed: Boolean): Completable =
        taskDao.updateCompleted(id, completed)
            .subscribeOn(Schedulers.io())
            .andThen(
                taskDao.getById(id)
                    .flatMapCompletable { task ->
                        task.remoteId?.let { remoteId ->
                            apiService.updateTask(remoteId.toString(), task.toDto())
                                .ignoreElement()
                                .onErrorComplete()
                        } ?: Completable.complete()
                    }
            )
}