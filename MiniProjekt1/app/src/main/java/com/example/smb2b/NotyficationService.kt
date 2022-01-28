package com.example.smb2b

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class NotyficationService : Service() {

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

        val inputLat = intent.getStringExtra("lat")
        val inputLng = intent.getStringExtra("lng")
        val enter = intent.getStringExtra("enter")
        val key = intent.getStringExtra("key")

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("${enter} strefę sklepu ${key}")
            .setContentText("Koordynaty: lat${inputLat}, lng${inputLng}")
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
//            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}