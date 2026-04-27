package com.cret.hilt_practice

import android.app.Application
import com.cret.hilt_practice.di.AppContainer

class HiltPracticeApplication : Application() {
    val container = AppContainer()
}
