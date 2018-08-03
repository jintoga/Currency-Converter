package com.jintoga.currencyconverter.ui.converter

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup.FOCUS_BEFORE_DESCENDANTS
import com.jintoga.currencyconverter.R
import com.jintoga.currencyconverter.appComponent
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
        swipeToRefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent))
        swipeToRefresh.setOnRefreshListener {
            presenter.stopUpdatingCurrencyRates()
            presenter.startUpdatingCurrencyRates()
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView.setHasFixedSize(true)
        //Prevent focusing in EditText immediately
        recyclerView.descendantFocusability = FOCUS_BEFORE_DESCENDANTS
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CurrencyConverterAdapter(this)
        recyclerView.adapter = adapter
    }

    override fun onBaseCurrencyChanged(rate: Rate) {
        presenter.setBaseCurrency(rate.code)
    }

    override fun onUpdating() {
        swipeToRefresh.isRefreshing = true
    }

    override fun onUpdated(currencyRates: List<Rate>) {
        swipeToRefresh.isRefreshing = false
        swipeToRefresh.isEnabled = false
        if (adapter.itemCount == 0) {
            //IMPORTANT: Use map for updating values so prevent ViewHolders from being recycled
            // so it won't mess up map's EditTexts
            recyclerView.setItemViewCacheSize(currencyRates.size)
            adapter.initRates(currencyRates)
        } else {
            adapter.updateRates(currencyRates)
        }
    }

    override fun onError(message: String) {
        swipeToRefresh.isRefreshing = false
        swipeToRefresh.isEnabled = true
        snackbar(rootView, message)
    }

}
