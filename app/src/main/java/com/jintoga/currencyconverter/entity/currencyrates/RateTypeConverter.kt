package com.jintoga.currencyconverter.entity.currencyrates

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class RateTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): TreeMap<String, Double> {
        if (data == null) {
            return TreeMap()
        }
        val listType = object : TypeToken<TreeMap<String, Double>>() {
        }.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: TreeMap<String, Double>): String {
        return gson.toJson(someObjects)
    }
}