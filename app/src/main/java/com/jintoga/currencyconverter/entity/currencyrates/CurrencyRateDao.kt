package com.jintoga.currencyconverter.entity.currencyrates

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Maybe

@Dao
interface CurrencyRateDao {
    @Query("SELECT * FROM CurrencyRates")
    fun get(): Maybe<CurrencyRates>

    @Insert(onConflict = REPLACE)
    fun insert(currencyRates: CurrencyRates)
}