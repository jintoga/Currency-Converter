package com.jintoga.currencyconverter.injection

import android.content.Context
import com.jintoga.currencyconverter.CCApp
import com.jintoga.currencyconverter.injection.modules.AppModule
import com.jintoga.currencyconverter.injection.modules.NetworkModule
import com.jintoga.currencyconverter.managers.currency.CurrencyManager
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [(AppModule::class), (NetworkModule::class)])
interface AppComponent {

    fun inject(activity: CCApp)

    fun context(): Context

    fun currencyManager(): CurrencyManager
}