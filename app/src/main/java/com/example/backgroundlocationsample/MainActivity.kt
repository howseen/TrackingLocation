package com.example.backgroundlocationsample

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    @SuppressLint("BatteryLife")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1.
        ignoreBatteryOptimising()

        // 2.
        NotifHelper.openDoNotDisturbSetting(this)

        // 3.
        if (hasLocationPermission()) {
            Intent(this, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                startService(this)
            }
        } else {
            requestLocationPermissions()
        }
    }

    private fun requestLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                11
            )
            return
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            11
        )
    }
}