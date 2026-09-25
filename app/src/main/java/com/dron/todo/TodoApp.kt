package com.dron.todo

import android.app.Application
import com.dron.todo.util.NotificationHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TodoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createChannel(this)
    }
}