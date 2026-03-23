package com.dev.nagdaadmin.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.dev.nagdaadmin.utils.FireStoreConstant.PREFS_NAME
import javax.inject.Inject


class SharedPrefManager @Inject constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun putString(key: String, value: String) = sharedPreferences.edit { putString(key, value) }
    fun putInt(key: String, value: Int)       = sharedPreferences.edit { putInt(key, value) }
    fun putLong(key: String, value: Long)     = sharedPreferences.edit { putLong(key, value) }
    fun putFloat(key: String, value: Float)   = sharedPreferences.edit { putFloat(key, value) }
    fun putBoolean(key: String, value: Boolean) = sharedPreferences.edit { putBoolean(key, value) }

    fun getString(key: String, default: String = "")      = sharedPreferences.getString(key, default) ?: default
    fun getInt(key: String, default: Int = 0)             = sharedPreferences.getInt(key, default)
    fun getLong(key: String, default: Long = 0L)          = sharedPreferences.getLong(key, default)
    fun getFloat(key: String, default: Float = 0f)        = sharedPreferences.getFloat(key, default)
    fun getBoolean(key: String, default: Boolean = false) = sharedPreferences.getBoolean(key, default)

    fun remove(key: String) = sharedPreferences.edit { remove(key) }
    fun clearAll()          = sharedPreferences.edit { clear() }
}
