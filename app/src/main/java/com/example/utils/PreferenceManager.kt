package com.example.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {
    private var pref: SharedPreferences? = null

    private val editor: SharedPreferences.Editor
        get() = pref!!.edit()

    fun init(context: Context) {
        if (pref == null) {
            pref = context.getSharedPreferences("CommonPreferences", Context.MODE_PRIVATE)
        } else {
            throw RuntimeException("Preference already initialized")
        }
    }

    fun delete(key: String) {
        if (pref!!.contains(key)) {
            editor.remove(key).apply()
        }
    }

    fun clear() {
        editor.clear().apply()
    }

    fun <T> getPref(key: String): T? {
        return pref!!.all[key] as T?
    }

    fun <T> getPref(key: String, defValue: T): T {
        val returnValue = pref!!.all[key] as T?
        return returnValue ?: defValue
    }

    fun savePref(key: String, value: Any?) {
        val editor = editor

        when {
            value is Boolean -> {
                editor.putBoolean(key, (value as Boolean?)!!)
            }
            value is Int -> {
                editor.putInt(key, (value as Int?)!!)
            }
            value is Float -> {
                editor.putFloat(key, (value as Float?)!!)
            }
            value is Long -> {
                editor.putLong(key, (value as Long?)!!)
            }
            value is String -> {
                editor.putString(key, value as String?)
            }
            value is Enum<*> -> {
                editor.putString(key, value.toString())
            }
            value != null -> {
                throw RuntimeException("Attempting to save non-primitive preference")
            }
        }

        editor.apply()
    }
}