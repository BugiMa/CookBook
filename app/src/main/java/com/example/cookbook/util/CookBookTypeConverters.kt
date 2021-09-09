package com.example.cookbook.util

import androidx.room.TypeConverter

class CookBookTypeConverters {

    @TypeConverter
    fun toString(array: ArrayList<String>): String {
        return array.joinToString(";")
    }

    @TypeConverter
    fun fromString(string: String): ArrayList<String> {
        return ArrayList(string.split(";"))
    }
}