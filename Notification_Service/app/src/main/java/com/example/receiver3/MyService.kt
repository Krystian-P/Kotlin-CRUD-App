package com.example.receiver3

import android.app.*
import android.content.ComponentName
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class MyService : Service() {

    private val channelId = "Notification from Service"
    @RequiresApi(Build.VERSION_CODES.O)


    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= 26) {
            val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel(
                    channelId,
                    "Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val input = intent.getStringExtra("str1")
        val notificationIntent = Intent(Intent.ACTION_MAIN);
        notificationIntent.setClassName("com.example.smb2b", "com.example.smb2b.DrugieActivity")
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(input)
            .setContentText(input)
            .setSmallIcon(R.drawable.download)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        return START_NOT_STICKY
    }
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}