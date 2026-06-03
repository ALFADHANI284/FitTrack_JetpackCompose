package com.aplikasi.fittrack

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("EXTRA_TITLE") ?: "Waktunya Latihan! "
        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: "Jadwal workout kamu udah tiba. Yuk gerak sekarang!"

        // Panggil helper notifikasi yang tadi kita bikin
        val notifHelper = NotificationHelper(context)
        notifHelper.showWorkoutSuccessNotification(title, message)
    }
}