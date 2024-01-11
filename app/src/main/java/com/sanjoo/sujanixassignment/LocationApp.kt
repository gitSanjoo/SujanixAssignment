package com.sanjoo.sujanixassignment

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.room.Room

class LocationApp:Application() {

    lateinit var locationTrackDB: LocationTrackDB
    override fun onCreate() {
        super.onCreate()
        locationTrackDB= Room
            .databaseBuilder(
                applicationContext,
                LocationTrackDB::class.java, LocationTrackDB.DB_NAME)
            .build()
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channel= NotificationChannel("location",
                "Location", NotificationManager.IMPORTANCE_LOW)

            val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object{
        var isTracking=false
    }
}