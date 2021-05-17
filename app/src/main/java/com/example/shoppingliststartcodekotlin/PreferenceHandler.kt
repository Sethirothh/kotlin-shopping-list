package com.example.shoppingliststartcodekotlin

import android.content.Context
import androidx.preference.PreferenceManager

//Singleton class to handle preferences from any fragment/activity
object PreferenceHandler {
    private const val SETTINGS_GENDERKEY = "male"
    private const val SETTINGS_NAMEKEY = "name"
    private const val SETTINGS_NOTIFICATONS = "notifications"

    fun useNotifications(context: Context) : Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_NOTIFICATONS,true)
    }
    //note these are "static" methods - meaning they always exists
    //so we do not have to create an instance of this class to
    //get the values - the Singleton pattern again!
    fun isMale(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_GENDERKEY, true)
    }

    fun getName(context: Context): String {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(SETTINGS_NAMEKEY, "")!!
    }


}