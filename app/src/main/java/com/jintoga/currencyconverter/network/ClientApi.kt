package com.jintoga.currencyconverter.network

import com.jintoga.currencyconverter.entity.CurrencyRates
import io.reactivex.Observable
import retrofit2.http.GET

interface ClientApi {
    @GET("latest?base=EUR")
    fun getCurrencyRates(): Observable<CurrencyRates>
}