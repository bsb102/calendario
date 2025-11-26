package com.example.calendario.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.calendario.utils.NotificationHelper

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val reminderId = intent.getIntExtra("reminder_id", 0)
        val title = intent.getStringExtra("title") ?: "Recordatorio"

        val notificationHelper = NotificationHelper(context)
        notificationHelper.showNotification(reminderId, title, "Es hora de tu recordatorio")
    }
}