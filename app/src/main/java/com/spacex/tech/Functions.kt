package com.spacex.tech

import android.app.Activity
import android.content.Context

object Functions {

    private const val APP_PREFERENCES = "mysettings"
    private const val CHECK = "CHECK"

    fun saveCheck(check: Boolean, activity: Activity){
        val pref = activity.application?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val editor = pref!!.edit()
        editor.putBoolean(CHECK, check)
        editor.apply()
    }

    fun getCheck(activity: Activity): Boolean? {
        val pref = activity.application?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        return pref?.getBoolean(CHECK, true)
    }
}