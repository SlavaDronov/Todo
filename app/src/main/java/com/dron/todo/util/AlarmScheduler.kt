package com.dron.todo.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.dron.todo.data.local.entity.TaskEntity

object AlarmScheduler {

    private const val TAG = "AlarmScheduler"

    /** За 15 минут до дедлайна */
    private const val REMINDER_OFFSET_MS = 15 * 60 * 1000L

    fun schedule(context: Context, task: TaskEntity) {
        if (task.completed) {
            Log.d(TAG, "Задача выполнена — будильник не нужен")
            cancel(context, task.id)
            return
        }

        val triggerAt = task.dueDate - REMINDER_OFFSET_MS
        if (triggerAt <= System.currentTimeMillis()) {
            Log.d(TAG, "Время напоминания уже прошло — пропускаем")
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.w(TAG, "Нет разрешения SCHEDULE_EXACT_ALARM — используем неточный будильник")
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAt,
                    buildPendingIntent(context, task)
                )
                return
            }
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAt,
            buildPendingIntent(context, task)
        )

        Log.d(TAG, "Будильник запланирован: taskId=${task.id}, triggerAt=$triggerAt")
    }

    fun cancel(context: Context, taskId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            Log.d(TAG, "Будильник отменён: taskId=$taskId")
        }
    }

    private fun buildPendingIntent(context: Context, task: TaskEntity): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.EXTRA_TASK_ID, task.id)
            putExtra(AlarmReceiver.EXTRA_TASK_TITLE, task.title)
            putExtra(AlarmReceiver.EXTRA_TASK_DESCRIPTION, task.description)
        }

        return PendingIntent.getBroadcast(
            context,
            task.id.toInt(),                      // requestCode = taskId
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}