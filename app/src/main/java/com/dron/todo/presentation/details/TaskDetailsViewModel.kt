package com.dron.todo.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.dron.todo.data.local.entity.TaskEntity
import com.dron.todo.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.BackpressureStrategy
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private var taskId: Long = -1L

    fun setTaskId(id: Long) {
        taskId = id
    }

    fun observeTask(): LiveData<TaskEntity> =
        repository.observeById(taskId)
            .toLiveData()
}