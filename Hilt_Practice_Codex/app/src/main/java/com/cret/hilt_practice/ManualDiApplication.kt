package com.cret.hilt_practice

import android.app.Application
import com.cret.hilt_practice.di.AppContainer

class ManualDiApplication : Application() {
    // This is the app-level object graph that Hilt would later manage for us.
    val appContainer = AppContainer()
}
