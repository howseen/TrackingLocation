package com.example.backgroundlocationsample

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

class LocationException(message: String) : Exception(message)

class LocationClient(
    private val context: Context,
    private val client: FusedLocationProviderClient,
) {

    @SuppressLint("MissingPermission")
    fun getLocationUpdates(
        callback: (Location) -> Unit,
    ) {
        if (!context.hasLocationPermission()) {
            logError(LocationException("Location permission problem"))
            return
        }

        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGpsEnabled && !isNetworkEnabled) {
            logError(LocationException("GPS/Network is not enabled"))
            return
        }

        val request = LocationRequest.create()
            .setInterval(1_000)
            .setFastestInterval(1_000)

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                result.locations.lastOrNull()?.let { location ->
                    callback(location)
                }
            }
        }

        client.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}