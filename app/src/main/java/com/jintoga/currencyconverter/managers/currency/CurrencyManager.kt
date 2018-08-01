package com.jintoga.currencyconverter.managers.currency

import com.jintoga.currencyconverter.entity.CurrencyRates
import io.reactivex.Observable

interface CurrencyManager {
    fun getCurrencyRates(): Observable<CurrencyRates>
}