package com.example.backgroundlocationsample

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat

fun logError(throwable: Throwable) {
    Log.e("DDDDD", "", throwable)
}

fun logDebug(text: String) {
    Log.d("DDDDD", text)
}


fun Context.hasLocationPermission(): Boolean {
    fun isPermissionGranted(permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    var result = isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION) &&
            isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        result = result && isPermissionGranted(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }

    return result
}

@SuppressLint("BatteryLife")
fun Activity.ignoreBatteryOptimising() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Intent().apply {
            val packageName = packageName
            val pm = getSystemService(POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }
    }
}