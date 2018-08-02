package com.jintoga.currencyconverter.entity.currencyrates

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class CurrencyRates(
        @ColumnInfo(name = "base")
        @SerializedName("base")
        var base: String? = null,

        @ColumnInfo(name = "date")
        @SerializedName("date")
        var date: String? = null,

        @TypeConverters(RateTypeConverter::class)
        @ColumnInfo(name = "rates")
        @SerializedName("rates")
        var rates: TreeMap<String, Double>? = null
) {

    @ColumnInfo(name = "id")
    @PrimaryKey
    var id: Long = 0

    val currencyRates: List<Rate>?
        get() = rates?.map {
            Rate(it.key, it.value)
        }
}