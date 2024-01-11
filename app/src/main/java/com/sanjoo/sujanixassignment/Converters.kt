package com.sanjoo.sujanixassignment

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromString(value: String?): ArrayList<LocationData> {
        val listType = object : TypeToken<ArrayList<LocationData?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<LocationData?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}