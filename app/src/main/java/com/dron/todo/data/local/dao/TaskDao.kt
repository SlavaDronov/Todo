package com.dron.todo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dron.todo.data.local.entity.TaskEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface TaskDao {

    // ---------- Чтение ----------

    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun observeAllSortedByCreatedAt(): Flowable<List<TaskEntity>>

    @Query("SELECT * FROM tasks ORDER BY priority DESC, createdAt DESC")
    fun observeAllSortedByPriority(): Flowable<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun observeById(id: Long): Flowable<TaskEntity>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getById(id: Long): Single<TaskEntity>

    @Query(
        """
        SELECT * FROM tasks
        WHERE title LIKE '%' || :query || '%'
           OR description LIKE '%' || :query || '%'
        ORDER BY createdAt DESC
        """
    )
    fun search(query: String): Flowable<List<TaskEntity>>

    // ---------- Запись ----------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: TaskEntity): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tasks: List<TaskEntity>): Completable

    @Update
    fun update(task: TaskEntity): Completable

    @Delete
    fun delete(task: TaskEntity): Completable

    @Query("DELETE FROM tasks WHERE id = :id")
    fun deleteById(id: Long): Completable

    @Query("UPDATE tasks SET completed = :completed WHERE id = :id")
    fun updateCompleted(id: Long, completed: Boolean): Completable
}