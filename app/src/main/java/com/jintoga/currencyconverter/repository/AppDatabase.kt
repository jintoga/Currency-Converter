package com.jintoga.currencyconverter.repository

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.jintoga.currencyconverter.entity.currencyrates.CurrencyRateDao
import com.jintoga.currencyconverter.entity.currencyrates.CurrencyRates
import com.jintoga.currencyconverter.entity.currencyrates.RateTypeConverter

@Database(entities = [(CurrencyRates::class)], version = 1)
@TypeConverters(RateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyRatesDao(): CurrencyRateDao

    companion object {

        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room
                        .databaseBuilder(
                                context.applicationContext,
                                AppDatabase::class.java,
                                "currencyconverter-db")
                        .allowMainThreadQueries()
                        .build()
            }
            return instance!!
        }

        fun destroyInstance() {
            instance = null
        }
    }
}