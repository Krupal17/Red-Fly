package com.narine.kp.redfly.messageDemo

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "AppPreferences"
        private const val IS_FIRST_TIME = "isFirstTime"

        @Volatile
        private var instance: SharedPreferenceManager? = null

        fun getInstance(context: Context): SharedPreferenceManager {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferenceManager(context).also { instance = it }
            }
        }
    }

    // Save a boolean value
    fun setIsFirstTime(isFirstTime: Boolean) {
        sharedPreferences.edit().putBoolean(IS_FIRST_TIME, isFirstTime).apply()
    }

    // Retrieve a boolean value
    fun isFirstTime(): Boolean {
        return sharedPreferences.getBoolean(IS_FIRST_TIME, true) // Default is true
    }

    // Save a string value
    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    // Retrieve a string value
    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    // Save an integer value
    fun saveInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    // Retrieve an integer value
    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    // Save a long value
    fun saveLong(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    // Retrieve a long value
    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    // Save a float value
    fun saveFloat(key: String, value: Float) {
        sharedPreferences.edit().putFloat(key, value).apply()
    }

    // Retrieve a float value
    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    // Save a boolean value
    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    // Retrieve a boolean value
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    // Clear all saved data
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}
