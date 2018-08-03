package com.jintoga.currencyconverter.managers.currency

import com.jintoga.currencyconverter.entity.currencyrates.CurrencyRates
import com.jintoga.currencyconverter.network.ClientApi
import com.jintoga.currencyconverter.repository.Repository
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class DefaultCurrencyManager(private val clientApi: ClientApi,
                             private val repository: Repository)
    : CurrencyManager {

    companion object {
        private const val UPDATE_FREQUENCY = 1L //1 second
    }

    override fun getCurrencyRates(base: String): Observable<CurrencyRates> {
        val sources = mutableListOf(
                getFromDb(),
                getFromApi(base)
        )
        return Observable.concat(sources)
    }

    private fun getFromDb(): Observable<CurrencyRates> =
            repository.getCurrencyRates()

    private fun getFromApi(base: String): Observable<CurrencyRates> =
            Observable
                    .interval(UPDATE_FREQUENCY, TimeUnit.SECONDS)
                    .flatMap {
                        clientApi.getCurrencyRates(base)
                    }
                    .doOnNext { saveToDb(it) }

    private fun saveToDb(currencyRates: CurrencyRates) {
        repository.saveCurrencyRates(currencyRates)
    }

}