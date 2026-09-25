package com.dron.todo.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra(EXTRA_TASK_ID, -1L)
        val title = intent.getStringExtra(EXTRA_TASK_TITLE) ?: "Задача"
        val description = intent.getStringExtra(EXTRA_TASK_DESCRIPTION)

        Log.d(TAG, "Будильник сработал: taskId=$taskId, title=$title")

        if (taskId <= 0) return

        NotificationHelper.showNotification(
            context = context,
            notificationId = taskId.toInt(),
            title = "Скоро дедлайн: $title",
            text = description ?: "Пора выполнить задачу"
        )
    }

    companion object {
        private const val TAG = "AlarmReceiver"

        const val EXTRA_TASK_ID = "extra_task_id"
        const val EXTRA_TASK_TITLE = "extra_task_title"
        const val EXTRA_TASK_DESCRIPTION = "extra_task_description"
    }
}