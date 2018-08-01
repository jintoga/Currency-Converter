package com.jintoga.currencyconverter.ui

import com.hannesdorfmann.mosby3.mvp.MvpActivity
import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView

abstract class BaseMvpActivity<V : MvpView, P : MvpPresenter<V>> : MvpActivity<V, P>()