package com.example.smb2b

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.smb2b.databinding.ActivityMapsBinding
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CircleOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapa: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    lateinit var db: FirebaseFirestore
    lateinit var geoClient: GeofencingClient


    private val shopCollectionReference= Firebase.firestore.collection("Shop_location")


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)

        perms()
        EventChangeListener()
        var id =0
        binding.bt1.setOnClickListener {
            LocationServices.getFusedLocationProviderClient(this).lastLocation
                .addOnSuccessListener {

                    val place = LatLng(it.latitude, it.longitude)
                    val nazwa =binding.et1.text.toString()
                    val opis = binding.et2.text.toString()
                    val lat = it.latitude.toString()
                    val lon = it.longitude.toString()
                    val radious = 100F
                    geoClient = LocationServices.getGeofencingClient(this)
                    val circleOptions = CircleOptions()
                        .center(place)
                        .radius(20.0)
                        .fillColor(0x40ff0000)
                        .strokeColor(Color.BLUE)
                        .strokeWidth(2f)

//                    geofenceList.add(Geofence.Builder()
//                        .setRequestId("${id++}")
//                        .setCircularRegion(place.latitude, place.longitude, radious)
//                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
//                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
//                        .build()
//                    )


                    val geo = Geofence.Builder().setRequestId("Geo${id++}")
                        .setCircularRegion(
                            place.latitude,
                            place.longitude,
                            radious
                        )
                        .setExpirationDuration(60*60*1000)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                        or Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build()
                    Toast.makeText(this, geo.toString(), Toast.LENGTH_SHORT).show()
                    GeofencingRequest.Builder()
                        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                        .addGeofence(geo)
                        .build()
                    getGeofencePendingIntent()

//                   geoClient.addGeofences(getGeofencingRequest(geo), getGeofencePendingIntent())
//                    .addOnSuccessListener {
//                    Toast.makeText(
//                    this@MapsActivity,
//                    "Geofence dodany.", Toast.LENGTH_SHORT).show()
//                }
//                    .addOnFailureListener {
//                    Toast.makeText(
//                    this@MapsActivity,
//                    "Geofence nie został dodany.", Toast.LENGTH_SHORT).show()
//                }

                    mapa.addMarker(MarkerOptions().position(place).title(binding.et1.text.toString()))
                    mapa.moveCamera(CameraUpdateFactory.newLatLng(place))
                    mapa.addCircle(circleOptions)

                    val shop = Shop(nazwa,opis, lat, lon)
                    saveShop(shop)
                }
        }
        binding.btShopList.setOnClickListener {
            val intentShopListActivity= Intent(this, ShopList::class.java)
            startActivity(intentShopListActivity)
        }
    }

    private fun getGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        .addGeofence(geofence)
        .build()
    }

    private fun getGeofencePendingIntent(): PendingIntent {
        return PendingIntent.getBroadcast(
        this,
        0,
        Intent(this, GeofenceReceiver::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        mapa = map
        perms()
        mapa.isMyLocationEnabled = true
    }

    fun perms(){
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        if(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ){
            requestPermissions(permissions, 1)
        }
    }
    private fun saveShop(shop: Shop) = CoroutineScope(Dispatchers.IO).launch {
        try {
            shopCollectionReference.add(shop).await()
            withContext(Dispatchers.Main){
                Toast.makeText(this@MapsActivity, "Succesfully saved", Toast.LENGTH_SHORT).show()
            }
        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@MapsActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("Shop_location")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                for (dc in snapshot!!.documentChanges) {
                    val marker = LatLng(dc.document.getString("lat")!!.toDouble(),dc.document.getString("lon")!!.toDouble() )
                    mapa.addMarker(MarkerOptions().position(marker).title(dc.document.getString("nazwa")))
                }

            }
    }

}