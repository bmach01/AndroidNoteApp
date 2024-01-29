package com.example.note

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.note.Model.AndroidAlarmScheduler
import com.example.note.Model.MainModel

class NoteApp: Application() {
    override fun onCreate() {
        super.onCreate()
        MainModel.getInstance().scheduler = AndroidAlarmScheduler(this)

        // notifications channel
        val channel = NotificationChannel(
            "NoteNotifications",
            "NoteNotificationsChannel",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}