package com.example.backgroundlocationsample

import android.app.Application

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        NotifHelper.registerNotifChannels()
    }

    companion object {
        var instance: MyApp? = null
    }

}
