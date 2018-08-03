package com.jintoga.currencyconverter.ui.converter

import com.jintoga.currencyconverter.entity.currencyrates.Rate
import com.jintoga.currencyconverter.managers.currency.CurrencyManager
import com.jintoga.currencyconverter.ui.BaseMvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class CurrencyConverterPresenter(private val currencyManager: CurrencyManager)
    : BaseMvpPresenter<CurrencyConverterContract.View>(),
        CurrencyConverterContract.UserActionListener {

    companion object {
        const val BASE_VALUE = 1.0
    }

    private var disposable: Disposable? = null
    private var baseCurrency = "EUR"

    override fun startUpdatingCurrencyRates() {
        disposable = currencyManager.getCurrencyRates(baseCurrency)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    view?.onUpdating()
                }
                .subscribe(
                        {
                            val ratesMap = it.ratesMap
                            if (ratesMap != null) {
                                val currencyRates = buildCurrencyRates(ratesMap)
                                view?.onUpdated(currencyRates)
                            }
                        },
                        {
                            view?.onError(it.localizedMessage)
                        })
        disposeOnDetachView(disposable)
    }

    private fun buildCurrencyRates(rates: TreeMap<String, Double>): List<Rate> {
        rates[baseCurrency] = BASE_VALUE

        val currencyRates = ArrayList<Rate>()
        for (code in rates.keys) {
            val value = rates[code] ?: continue
            if (code == baseCurrency) {
                currencyRates.add(0, Rate(code, value))
            } else {
                currencyRates.add(Rate(code, value))
            }
        }
        return currencyRates
    }

    override fun stopUpdatingCurrencyRates() {
        disposable?.dispose()
    }

    override fun setBaseCurrency(code: String) {
        stopUpdatingCurrencyRates()
        this.baseCurrency = code
        startUpdatingCurrencyRates()
    }

}