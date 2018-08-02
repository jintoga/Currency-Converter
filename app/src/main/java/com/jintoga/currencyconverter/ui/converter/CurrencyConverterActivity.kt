package com.jintoga.currencyconverter.ui.converter

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.ViewGroup.FOCUS_BEFORE_DESCENDANTS
import com.jintoga.currencyconverter.R
import com.jintoga.currencyconverter.appComponent
import com.jintoga.currencyconverter.entity.currencyrates.CurrencyRates
import com.jintoga.currencyconverter.entity.currencyrates.Rate
import com.jintoga.currencyconverter.ui.BaseMvpActivity
import com.jintoga.currencyconverter.ui.converter.adapter.CurrencyConverterAdapter
import kotlinx.android.synthetic.main.activity_currency_converter.*
import org.jetbrains.anko.design.snackbar

class CurrencyConverterActivity
    : BaseMvpActivity<CurrencyConverterContract.View, CurrencyConverterContract.UserActionListener>(),
        CurrencyConverterContract.View,
        CurrencyConverterAdapter.ActionListener {

    private lateinit var adapter: CurrencyConverterAdapter

    override fun createPresenter(): CurrencyConverterContract.UserActionListener =
            CurrencyConverterPresenter(appComponent.currencyManager())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_converter)
        init()
    }

    override fun onStart() {
        super.onStart()
        presenter.startUpdatingCurrencyRates()
    }

    override fun onStop() {
        super.onStop()
        presenter.stopUpdatingCurrencyRates()
    }

    private fun init() {
        recyclerView.setHasFixedSize(true)
        //Prevent focusing in EditText immediately
        recyclerView.descendantFocusability = FOCUS_BEFORE_DESCENDANTS
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CurrencyConverterAdapter(this)
        recyclerView.adapter = adapter
    }

    override fun onRateValueChanged(rateValue: Double) {
        Log.d("T", rateValue.toString())
        //ToDO
    }

    override fun onRateFocusChanged(rate: Rate) {
        adapter.moveItemToFirstPosition(rate)
    }

    override fun onUpdating() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onUpdated(currencyRates: CurrencyRates) {
        progressBar.visibility = View.GONE
        currencyRates.currencyRates?.let {
            adapter.updateRates(it)
        }
    }

    override fun onError(message: String) {
        progressBar.visibility = View.GONE
        snackbar(rootView, message)
    }
}
