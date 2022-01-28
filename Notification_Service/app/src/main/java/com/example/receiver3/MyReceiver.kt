package com.example.receiver3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

import android.view.View
import androidx.core.content.ContextCompat


class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //Toast.makeText(
            //context,
            //"Receiver odebrał: "+intent.getStringExtra("str1"),
            //Toast.LENGTH_SHORT
        //).show()

        val str1: String = intent.getStringExtra("str1").toString()
        val serviceIntent = Intent(context, MyService::class.java)
        serviceIntent.putExtra("str1", str1)
        ContextCompat.startForegroundService(context,serviceIntent)
    }


}



