package com.example.backgroundlocationsample

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.android.gms.location.LocationServices
import java.util.concurrent.Executors

class LocationService : Service() {

    private lateinit var locationClient: LocationClient
    private val executor = Executors.newSingleThreadExecutor()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = LocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        executor.execute {
            kotlin.runCatching {
                locationClient.getLocationUpdates { location ->
                    val lat = location.latitude.toString().takeLast(3)
                    val lng = location.longitude.toString().takeLast(3)
                    val text = "Location: ($lat, $lng)"
                    NotifHelper.updateLocationNotif(text)
                    logDebug(text)
                }
            }.getOrElse {
                logError(it)
            }

            startForeground(NotifHelper.NOTIF_LOCATION_ID, NotifHelper.getLocationNotif().build())
        }
    }

    private fun stop() {
        executor.execute {
            stopForeground(true)
            stopSelf()
        }
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

}
