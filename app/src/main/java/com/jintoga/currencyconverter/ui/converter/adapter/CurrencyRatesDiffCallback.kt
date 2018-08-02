package com.jintoga.currencyconverter.ui.converter.adapter

import android.support.v7.util.DiffUtil
import com.jintoga.currencyconverter.entity.currencyrates.Rate

class CurrencyRatesDiffCallback(private val oldRates: List<Rate>,
                                private val newRates: List<Rate>)
    : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldRates.size

    override fun getNewListSize(): Int = newRates.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldRates[oldItemPosition].code == newRates[newItemPosition].code

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldRates[oldItemPosition].value == newRates[newItemPosition].value
}