package com.sanjoo.sujanixassignment

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.ArrayList
import java.util.Locale

class LocationService:Service() {


    private val serviceScope= CoroutineScope(SupervisorJob()+ Dispatchers.IO)
    var locationClient: LocationClient?=null
    private val binder = LocalBinder()
    private val locationDataList=ArrayList<LocationData>()
    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }
    override fun onCreate() {
        super.onCreate()
        locationClient=DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            ACTION_START->start()
            ACTION_STOP->stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }
    private fun start(){

        val notification= NotificationCompat.Builder(this,"location")
            .setContentTitle("Tracking location.....")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)

        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient?.getLocationUpdate(5000L)
            ?.catch { e->e.printStackTrace() }
            ?.onEach { location ->
                var address=getAddress(location)
                val updateNotification=notification.setContentText(
                    "Location: (${location.latitude},${location.longitude} ,$address)"
                )
                Log.d("locationLog","Location: (${location.latitude},${location.longitude} ,$address)")
                notificationManager.notify(1,updateNotification.build())
                locationDataList.add(LocationData(System.currentTimeMillis(),location.latitude,location.longitude,address))
            }?.launchIn(serviceScope)
        startForeground(1,notification.build())
    }




    private fun stop(){
        serviceScope.launch(Dispatchers.IO){
            (applicationContext as LocationApp).locationTrackDB.tracksDao()
                .addTrack(
                    LocationTrackEntity(locationDataList.first().timestamp,
                        System.currentTimeMillis(),
                        locationDataList)
                )
        }.invokeOnCompletion {
            locationDataList.clear()
            stopForeground(true)
            stopSelf()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
    companion object{
        const val ACTION_START="ACTION_START"
        const val ACTION_STOP="ACTION_STOP"
    }

    private fun getAddress(location: Location):String {
        var address = ""
        val geocoder = Geocoder(this, Locale.getDefault())

        geocoder.getFromLocation(location.latitude, location.longitude,1).let {
            address=it?.get(0)?.subLocality+"\n"+it?.get(0)?.locality
        }
        return address
    }

    inner class LocalBinder : Binder() {
        fun getService(): LocationService = this@LocationService
    }
}