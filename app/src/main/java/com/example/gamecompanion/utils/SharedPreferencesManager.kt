package com.example.gamecompanion.utils

import android.content.Context

class SharedPreferencesManager {

    val userPreferencesFileName = "userPreferences"
    val usernameKey="username"
    //val context: Context? = null;

    fun getUsername(context: Context):String?{
        val sharedPreferences = context.getSharedPreferences(userPreferencesFileName, Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", null)
    }

    fun setUsername(context: Context, username: String?){
        val sharedPreferences = context.getSharedPreferences(userPreferencesFileName, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(usernameKey,username).apply()
    }

    fun clear(context: Context){
        val sharedPreferences = context.getSharedPreferences(userPreferencesFileName, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
}