package com.example.myphotocollections.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPrefManager {

    private const val PREF_NAME = "MyPrefs"
    private lateinit var preferences: SharedPreferences
    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun putString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String): String? {
        return preferences.getString(key, defaultValue)
    }
}