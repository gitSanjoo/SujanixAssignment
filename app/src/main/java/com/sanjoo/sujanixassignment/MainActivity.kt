package com.sanjoo.sujanixassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            ),0
        )
        setContentView(R.layout.activity_main)
        val btnStart: Button =findViewById(R.id.stat_btn)
        val btnStop:Button=findViewById(R.id.stop_btn)

        btnStart.setOnClickListener(View.OnClickListener {
            Intent(applicationContext,LocationService::class.java).apply {
                action=LocationService.ACTION_START
                startService(this)
            }
        })
        btnStop.setOnClickListener(View.OnClickListener {
            Intent(applicationContext,LocationService::class.java).apply {
                action=LocationService.ACTION_STOP
                startService(this)

            }
        })
    }
}