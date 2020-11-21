package com.example.groupfinder.util

import android.content.Context

class PreferenceProvider(context: Context) {


    private val sharedPreferences = context.getSharedPreferences("myPref",0)



    fun putEmailPreference(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getEmailPreference(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun putIdPreference(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getIdPreference(key: String): Int? {
        return sharedPreferences.getInt(key, 0)
    }

    // Clear shared preference
    fun clear() {
        println("Cleared")
        sharedPreferences.edit().clear().apply()
    }

}