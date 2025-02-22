package com.tuguego.app

import android.app.Application
import com.tuguego.app.core.di.coreModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.osmdroid.config.Configuration

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences("osmdroid", MODE_PRIVATE)
        )

        startKoin {
            androidContext(this@MyApp)
            modules(coreModule)
        }
    }
}