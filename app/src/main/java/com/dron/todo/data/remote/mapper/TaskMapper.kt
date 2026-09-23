package com.dron.todo.data.remote.mapper

import com.dron.todo.data.local.entity.TaskEntity
import com.dron.todo.data.remote.dto.TaskDto

// ---------- DTO → Entity (сеть → Room) ----------

fun TaskDto.toEntity(): TaskEntity = TaskEntity(
    id = 0L,                              // локальный id — Room сгенерирует сам
    remoteId = id?.toLongOrNull(),        // MockAPI id → Long, если получится
    title = title,
    description = description,
    priority = priority,
    dueDate = dueDate,
    createdAt = System.currentTimeMillis(),
    completed = completed
)

fun List<TaskDto>.toEntities(): List<TaskEntity> = map { it.toEntity() }

// ---------- Entity → DTO (Room → сеть) ----------

fun TaskEntity.toDto(): TaskDto = TaskDto(
    id = remoteId?.toString(),            // Long → String для MockAPI
    title = title,
    description = description,
    priority = priority,
    dueDate = dueDate,
    completed = completed
)