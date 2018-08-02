package com.jintoga.currencyconverter.repository

import com.jintoga.currencyconverter.entity.currencyrates.CurrencyRates
import io.reactivex.Observable

class RepositoryManager(private val database: AppDatabase) : Repository {

    override fun getCurrencyRates(): Observable<CurrencyRates> =
            database.currencyRatesDao().get().toObservable()

    override fun saveCurrencyRates(currencyRates: CurrencyRates) {
        database.currencyRatesDao().insert(currencyRates)
    }
}