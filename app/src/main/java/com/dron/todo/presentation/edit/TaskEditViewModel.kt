package com.dron.todo.presentation.edit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.dron.todo.data.local.entity.TaskEntity
import com.dron.todo.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TaskEditViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private var taskId: Long = -1L
    private var existingTask: TaskEntity? = null

    fun setTaskId(id: Long) {
        taskId = id
    }

    fun isEditMode(): Boolean = taskId > 0

    fun observeTask(): LiveData<TaskEntity>? {
        if (taskId <= 0) return null
        return repository.observeById(taskId)
            .toLiveData()
    }

    fun save(
        title: String,
        description: String?,
        priority: Int,
        dueDate: Long,
        completed: Boolean,
        onSuccess: () -> Unit
    ) {
        if (title.isBlank()) {
            Log.e(TAG, "Название не может быть пустым")
            return
        }

        val task = if (existingTask != null) {
            existingTask!!.copy(
                title = title.trim(),
                description = description?.trim()?.takeIf { it.isNotBlank() },
                priority = priority,
                dueDate = dueDate,
                completed = completed
            )
        } else {
            TaskEntity(
                id = 0L,
                title = title.trim(),
                description = description?.trim()?.takeIf { it.isNotBlank() },
                priority = priority,
                dueDate = dueDate,
                completed = completed,
                createdAt = System.currentTimeMillis()
            )
        }

        val operation = if (taskId > 0) {
            repository.update(task)
        } else {
            repository.create(task)
        }

        disposable.add(
            operation
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())   // ← ДОБАВЬ ЭТО
                .subscribe(
                    {
                        Log.d(TAG, if (taskId > 0) "Задача обновлена" else "Задача создана")
                        onSuccess()   // ← теперь в main thread
                    },
                    { error -> Log.e(TAG, "Ошибка сохранения: ${error.message}") }
                )
        )
    }

    fun setExistingTask(task: TaskEntity) {
        existingTask = task
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    companion object {
        private const val TAG = "TaskEditViewModel"
    }
}