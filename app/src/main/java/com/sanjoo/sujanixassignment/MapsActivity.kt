package com.sanjoo.sujanixassignment

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.sanjoo.sujanixassignment.databinding.ActivityMapsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var map: GoogleMap
    lateinit var binding:ActivityMapsBinding

    private lateinit var locationService: LocationService
    private var mBound: Boolean = false


    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            val binder = service as LocationService.LocalBinder
            locationService = binder.getService()
            mBound = true
            setLocationListener()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_maps)
        requestForPermissions()
        initUi()
        fetchLocationTracks()
    }

    private fun fetchLocationTracks(){
        lifecycleScope.launch(Dispatchers.IO) {
            val tracks=(applicationContext as LocationApp).locationTrackDB.tracksDao().getAllTracks()
            Log.d("logTracks",tracks.toString()+"::"+tracks.lastOrNull()?.endTimestamp)
        }
    }

    private fun initUi() {
        binding.startLocationTracking.setOnClickListener {
            Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                startService(this)
                bindService(this,connection,Context.BIND_AUTO_CREATE)
            }
        }
        binding.stopLocationTracking.setOnClickListener {
            Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_STOP
                startService(this)
                unbindService(connection)
                mBound = false
            }
        }
        mapFragment=SupportMapFragment()
        supportFragmentManager.beginTransaction().add(binding.mapView.id,mapFragment).commitNow()
        mapFragment.getMapAsync(this)
    }

    private fun setLocationListener(){
        locationService.locationClient?.getLocationUpdate(5000L)
            ?.catch { e->e.printStackTrace() }
            ?.onEach { location ->
                zoomToCurrentLocation(location)
            }?.launchIn(lifecycleScope)
    }



    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        this.map=map
        map.isMyLocationEnabled=true

        LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnCompleteListener {
            zoomToCurrentLocation(it.result)
        }

    }

    private fun zoomToCurrentLocation(location: Location) {
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(location.latitude, location.longitude),
                15f
            )
        )

    }

    private fun requestForPermissions(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            ),0
        )
    }

}