package com.jintoga.currencyconverter.repository

import com.jintoga.currencyconverter.entity.currencyrates.CurrencyRates
import io.reactivex.Observable

interface Repository {
    fun getCurrencyRates(): Observable<CurrencyRates>
    fun saveCurrencyRates(currencyRates: CurrencyRates)
}