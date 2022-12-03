package com.example.backgroundlocationsample

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat

object NotifHelper {
    const val CHANNEL_ID = "location"
    const val CHANNEL_NAME = "Location"
    const val NOTIF_LOCATION_ID = 1

    private val context = MyApp.instance!!

    private val notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun openDoNotDisturbSetting(activity: Activity) {
        // Check if the notification policy access has been granted for the app.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!notifManager.isNotificationPolicyAccessGranted) {
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                activity.startActivity(intent)
            }
        }
    }

    fun registerNotifChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )

            notifManager.createNotificationChannel(channel)
        }
    }

    fun getLocationNotif(): NotificationCompat.Builder =
        NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Tracking location...")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)

    fun updateLocationNotif(text: String) {
        val updatedNotif = getLocationNotif().setContentText(text)
        notifManager.notify(NOTIF_LOCATION_ID, updatedNotif.build())
    }
}