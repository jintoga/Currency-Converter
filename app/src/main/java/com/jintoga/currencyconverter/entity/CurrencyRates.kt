package com.jintoga.currencyconverter.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class CurrencyRates(
        @SerializedName("base") val base: String? = null,
        @SerializedName("date") val date: String? = null,
        @SerializedName("rates") private val rates: TreeMap<String, Float>? = null
) {
    val currencyRates: List<Rate>?
        get() = rates?.map {
            Rate(it.key, it.value)
        }
}