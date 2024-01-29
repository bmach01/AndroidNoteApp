package com.example.note.Model

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.note.R

class AlarmReceiver: BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        Log.d("alarm", "Alarm triggered: $message")
        showNotification(context, intent)
    }
    fun showNotification(context: Context?, intent: Intent?) {
        val notificationManager = ContextCompat.getSystemService(context!!, NotificationManager::class.java)
        val notification = NotificationCompat.Builder(context, "NoteNotifications")
            .setContentText(intent?.getStringExtra("EXTRA_MESSAGE"))
            .setContentTitle("Note przypomnienie!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        notificationManager?.notify(1, notification)
    }
}