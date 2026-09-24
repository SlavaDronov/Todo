package com.dron.todo.presentation.list

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dron.todo.R
import com.dron.todo.data.local.entity.TaskEntity
import com.dron.todo.databinding.ItemTaskBinding

class TaskListAdapter(
    private val onTaskClick: (TaskEntity) -> Unit,
    private val onCompletedToggle: (TaskEntity, Boolean) -> Unit
) : ListAdapter<TaskEntity, TaskListAdapter.TaskViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(
        private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: TaskEntity) {
            binding.titleText.text = task.title

            // Описание — скрываем, если пусто
            if (task.description.isNullOrBlank()) {
                binding.descriptionText.visibility = android.view.View.GONE
            } else {
                binding.descriptionText.visibility = android.view.View.VISIBLE
                binding.descriptionText.text = task.description
            }

            // Дата — пока формат timestamp (потом заменим на нормальный формат)
            binding.dueDateText.text = task.dueDate.toString()

            // Чекбокс
            binding.completedCheckbox.setOnCheckedChangeListener(null)
            binding.completedCheckbox.isChecked = task.completed
            binding.completedCheckbox.setOnCheckedChangeListener { _, isChecked ->
                onCompletedToggle(task, isChecked)
            }

            // Зачёркиваем название, если выполнено
            binding.titleText.paintFlags = if (task.completed) {
                binding.titleText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.titleText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            // Цвет полоски приоритета
            val color = when (task.priority) {
                0 -> 0xFF4CAF50.toInt() // зелёный — низкий
                1 -> 0xFFFFC107.toInt() // жёлтый — средний
                2 -> 0xFFF44336.toInt() // красный — высокий
                else -> 0xFF9E9E9E.toInt() // серый — неизвестный
            }
            binding.priorityIndicator.setBackgroundColor(color)

            // Клик по карточке
            binding.root.setOnClickListener {
                onTaskClick(task)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TaskEntity>() {
            override fun areItemsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}