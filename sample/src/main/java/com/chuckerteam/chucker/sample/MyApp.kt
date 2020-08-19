package com.chuckerteam.chucker.sample

import android.app.Application
import com.chuckerteam.chucker.api.Chucker
import com.chuckerteam.chucker.api.ExceptionCollector

class MyApp:Application() {

    override fun onCreate() {
        super.onCreate()
       ExceptionCollector.init(this)
    }
}