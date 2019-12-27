package com.example.tagihin.utils

import android.content.Context

class PreferencesHelper(var context : Context){
    companion object{
        const val PREF_FILE = "PREF_TAGIHIN"
        const val ISLOGIN = "ISLOGIN"
        const val NAME = "NAME"
        const val CATER = "CATER"
        const val USERNAME = "USERNAME"
        const val PROFILEPIC = "PROFILEPIC"
        const val PRIVILEGE = "PRIVILEGE"
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

    fun setProfilePicUrl(url : String){
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        val editor = settings.edit()
        editor.putString(PROFILEPIC, url)
        editor.apply()
    }

    fun getProfilePicUrl() : String?{
        return context.getSharedPreferences(PREF_FILE,0).getString(PROFILEPIC,"")
    }

    fun getPrivilege() : Int{
        return context.getSharedPreferences(PREF_FILE,0).getInt(PRIVILEGE,0)
    }

    fun setPrivilege(privilage : Int){
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        val editor = settings.edit()
        editor.putInt(PRIVILEGE, privilage)
        editor.apply()
    }
}