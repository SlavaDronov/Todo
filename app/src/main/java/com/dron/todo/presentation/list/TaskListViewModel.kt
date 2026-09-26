package com.dron.todo.presentation.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.dron.todo.data.local.entity.TaskEntity
import com.dron.todo.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val sortMode = BehaviorSubject.createDefault(SortMode.BY_DATE)

    /** Поток поисковых запросов (с начальным пустым значением) */
    private val searchQuery = BehaviorSubject.createDefault("")

    val tasks: LiveData<List<TaskEntity>> = searchQuery
        .debounce(300, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .toFlowable(BackpressureStrategy.LATEST)
        .switchMap { query ->
            if (query.isBlank()) {
                sortMode.toFlowable(BackpressureStrategy.LATEST)
                    .switchMap { mode ->
                        when (mode) {
                            SortMode.BY_DATE -> repository.observeAllSortedByCreatedAt()
                            SortMode.BY_PRIORITY -> repository.observeAllSortedByPriority()
                        }
                    }
            } else {
                repository.search(query)
            }
        }
        .toLiveData()

    init {
        refresh()
    }

    fun refresh() {
        disposable.add(
            repository.refreshFromNetwork()
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { Log.d(TAG, "Данные синхронизированы") },
                    { error -> Log.e(TAG, "Ошибка синхронизации: ${error.message}") }
                )
        )
    }

    fun setSortMode(mode: SortMode) {
        if (sortMode.value != mode) {
            sortMode.onNext(mode)
        }
    }

    fun search(query: String) {
        searchQuery.onNext(query)
    }

    fun delete(task: TaskEntity) {
        disposable.add(
            repository.delete(task)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { Log.d(TAG, "Задача удалена: ${task.id}") },
                    { error -> Log.e(TAG, "Ошибка удаления: ${error.message}") }
                )
        )
    }

    fun toggleCompleted(id: Long, completed: Boolean) {
        disposable.add(
            repository.toggleCompleted(id, completed)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { Log.d(TAG, "Статус обновлён: $id → $completed") },
                    { error -> Log.e(TAG, "Ошибка обновления: ${error.message}") }
                )
        )
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    companion object {
        private const val TAG = "TaskListViewModel"
    }
}