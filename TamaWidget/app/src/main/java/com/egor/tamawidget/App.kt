package com.egor.tamawidget

import android.app.Application
import com.egor.tamawidget.work.PetTickWorker

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        PetTickWorker.schedule(this)
    }
}