package com.example.smb2b


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class GeofenceReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        val geoEvent = GeofencingEvent.fromIntent(intent)

        val lat: String = intent.getStringExtra("lat").toString()
        val lng: String = intent.getStringExtra("lng").toString()
        val key: String = intent.getStringExtra("key").toString()

        val serviceIntent = Intent(context, NotyficationService::class.java)




        if(geoEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
//            Log.e("geofences", "Wejście: ${geoEvent.triggeringLocation.toString()}")
            val enter = "Wkroczono w"
            serviceIntent.putExtra("enter", enter)
            serviceIntent.putExtra("lat", lat)
            serviceIntent.putExtra("lng", lng)
            serviceIntent.putExtra("key", key)
            ContextCompat.startForegroundService(context, serviceIntent)
        }else {
            Log.e("geofences", "Wyjście: ${geoEvent.triggeringLocation}")
            val enter = "Opuszczono"
            serviceIntent.putExtra("enter", enter)
            serviceIntent.putExtra("lat", lat)
            serviceIntent.putExtra("lng", lng)
            serviceIntent.putExtra("key", key)
            ContextCompat.startForegroundService(context, serviceIntent)
        }
    }
}