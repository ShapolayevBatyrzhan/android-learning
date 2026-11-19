package com.example.trainjun

import android.app.Application
import kotlin.getValue

class TrainJunApp : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}