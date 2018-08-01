package com.jintoga.currencyconverter.managers.currency

import com.jintoga.currencyconverter.entity.CurrencyRates
import com.jintoga.currencyconverter.network.ClientApi
import io.reactivex.Observable

class DefaultCurrencyManager constructor(private val clientApi: ClientApi)
    : CurrencyManager {

    override fun getCurrencyRates(): Observable<CurrencyRates> = clientApi.getCurrencyRates()

}