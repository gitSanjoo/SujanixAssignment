package com.sanjoo.sujanixassignment

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Locale

class LocationService:Service() {
    private val serviceScope= CoroutineScope(SupervisorJob()+ Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    override fun onBind(intent: Intent?): IBinder? {
        return null
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

        locationClient.getLocationUpdate(100000L)
            .catch { e->e.printStackTrace() }
            .onEach { location ->
                val lat=location.latitude.toString()
                val long=location.longitude.toString()
//                var country=getCountryName(lat,long)
                var city=getCityName(lat,long)

                val updateNotification=notification.setContentText(
                    "Location: ($lat,$long ,$city)"
                )
                notificationManager.notify(1,updateNotification.build())
            }.launchIn(serviceScope)
        startForeground(1,notification.build())
    }
    private fun stop(){
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
    companion object{
        const val ACTION_START="ACTION_START"
        const val ACTION_STOP="ACTION_STOP"
    }



    //fun getCountryName(lt: String, lg: String):String {
//    var countryName = ""
//    var geocoder = Geocoder(this, Locale.getDefault())
//    var address = geocoder.getFromLocation(lt.toDouble(), lg.toDouble(),1)
//
//    if (address != null) {
//        countryName=address.get(0).countryName
//    }
//    return countryName
//}
    fun getCityName(lt: String, lg: String):String {
        var cityName = ""
        var geocoder = Geocoder(this, Locale.getDefault())
        var address = geocoder.getFromLocation(lt.toDouble(), lg.toDouble(),3)

        if (address != null) {
            cityName=address.get(0).locality
        }
        return cityName
    }
}