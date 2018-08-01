package com.jintoga.currencyconverter.injection.modules

import android.content.Context
import com.jintoga.currencyconverter.CCApp
import com.jintoga.currencyconverter.managers.currency.CurrencyManager
import com.jintoga.currencyconverter.managers.currency.DefaultCurrencyManager
import com.jintoga.currencyconverter.network.ClientApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: CCApp) {

    @Provides
    fun provideContext(): Context = application

    @Provides
    @Singleton
    fun provideApplication(): CCApp = application

    @Provides
    @Singleton
    fun provideCurrencyManager(clientApi: ClientApi): CurrencyManager =
            DefaultCurrencyManager(clientApi)

}