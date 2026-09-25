package com.dron.todo.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateTimeUtils {

    private val displayFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    fun format(timestamp: Long): String {
        if (timestamp <= 0L) return ""
        return displayFormat.format(Date(timestamp))
    }

    fun parse(text: String): Long {
        return try {
            displayFormat.parse(text)?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    fun currentTimeMillis(): Long = System.currentTimeMillis()

    fun toCalendar(timestamp: Long): Calendar {
        return Calendar.getInstance().apply {
            timeInMillis = if (timestamp > 0) timestamp else System.currentTimeMillis()
        }
    }
}