package com.chuckerteam.chucker.sample

import android.app.Application
import com.chuckerteam.chucker.api.Chucker

class MyApp:Application() {

    override fun onCreate() {
        super.onCreate()
        Chucker.init(this)
    }
}