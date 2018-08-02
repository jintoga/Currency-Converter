package com.jintoga.currencyconverter.ui.converter

import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.jintoga.currencyconverter.entity.currencyrates.CurrencyRates

interface CurrencyConverterContract {
    interface View : MvpView {
        fun onUpdating()
        fun onUpdated(currencyRates: CurrencyRates)
        fun onError(message: String)
    }

    interface UserActionListener : MvpPresenter<View> {
        fun startUpdatingCurrencyRates()
        fun stopUpdatingCurrencyRates()
    }
}