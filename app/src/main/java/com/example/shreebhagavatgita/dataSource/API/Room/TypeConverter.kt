package com.example.shreebhagavatgita.dataSource.API.Room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class TypeConverter {

    @TypeConverter
    fun fromListToString(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToList(list: String): List<String>{
        return Gson().fromJson(list, object : TypeToken<List<String>>(){}.type)
    }
}