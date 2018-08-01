package com.jintoga.currencyconverter

import android.app.Application
import android.content.Context
import com.jintoga.currencyconverter.injection.AppComponent
import com.jintoga.currencyconverter.injection.DaggerAppComponent
import com.jintoga.currencyconverter.injection.modules.AppModule

class CCApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent
            private set
    }

    operator fun get(context: Context): Application =
            context.applicationContext as Application

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        appComponent.inject(this)
    }
}