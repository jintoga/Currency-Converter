package com.jintoga.currencyconverter.network

import com.jintoga.currencyconverter.entity.currencyrates.CurrencyRates
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ClientApi {
    @GET("latest")
    fun getCurrencyRates(@Query("base") code: String): Observable<CurrencyRates>
}