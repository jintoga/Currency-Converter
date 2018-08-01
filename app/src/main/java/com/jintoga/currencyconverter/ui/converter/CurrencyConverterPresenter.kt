package com.jintoga.currencyconverter.ui.converter

import com.jintoga.currencyconverter.managers.currency.CurrencyManager
import com.jintoga.currencyconverter.ui.BaseMvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CurrencyConverterPresenter(private val currencyManager: CurrencyManager)
    : BaseMvpPresenter<CurrencyConverterContract.View>(),
        CurrencyConverterContract.UserActionListener {

    private var disposable: Disposable? = null

    override fun startUpdatingCurrencyRates() {
        disposable = currencyManager.getCurrencyRates()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    view?.onUpdating()
                }
                .subscribe(
                        {
                            view?.onUpdated(it)
                        },
                        {
                            view?.onError(it.localizedMessage)
                        })
        disposeOnDetachView(disposable)
    }

    override fun stopUpdatingCurrencyRates() {
        disposable?.dispose()
    }

}