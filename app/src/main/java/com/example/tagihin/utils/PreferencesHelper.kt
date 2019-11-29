package com.example.tagihin.utils

import android.content.Context

class PreferencesHelper(var context : Context){
    companion object{
        const val PREF_FILE = "PREF_TAGIHIN"
        const val ISLOGIN = "ISLOGIN"
        const val NAME = "NAME"
        const val CATER = "CATER"
        const val USERNAME = "USERNAME"
    }

    fun getLoginStatus() : Boolean {
        return context.getSharedPreferences(PREF_FILE,0).getBoolean(ISLOGIN,false)
    }

    fun setLoginStatus(status : Boolean){
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        val editor = settings.edit()
        editor.putBoolean(ISLOGIN, status)
        editor.apply()
    }

    fun getName() : String? {
        return context.getSharedPreferences(PREF_FILE,0).getString(NAME,"")
    }

    fun setName(name : String) {
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        val editor = settings.edit()
        editor.putString(NAME, name)
        editor.apply()
    }

    fun getUsername() : String? {
        return context.getSharedPreferences(PREF_FILE,0).getString(USERNAME,"")
    }

    fun setUsername(username : String) {
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        val editor = settings.edit()
        editor.putString(USERNAME, username)
        editor.apply()
    }

    fun getCater() : String? {
        return context.getSharedPreferences(PREF_FILE,0).getString(CATER,"")
    }
}