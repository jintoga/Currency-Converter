package com.jintoga.currencyconverter.injection.modules

import android.content.Context
import com.jintoga.currencyconverter.CCApp
import com.jintoga.currencyconverter.managers.currency.CurrencyManager
import com.jintoga.currencyconverter.managers.currency.DefaultCurrencyManager
import com.jintoga.currencyconverter.network.ClientApi
import com.jintoga.currencyconverter.repository.AppDatabase
import com.jintoga.currencyconverter.repository.Repository
import com.jintoga.currencyconverter.repository.RepositoryManager
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
    fun provideAppDataBase(): AppDatabase =
            AppDatabase.getInstance(application)

    @Provides
    @Singleton
    fun provideRepository(appDatabase: AppDatabase): Repository =
            RepositoryManager(appDatabase)

    @Provides
    @Singleton
    fun provideCurrencyManager(clientApi: ClientApi,
                               repository: Repository): CurrencyManager =
            DefaultCurrencyManager(clientApi, repository)

}