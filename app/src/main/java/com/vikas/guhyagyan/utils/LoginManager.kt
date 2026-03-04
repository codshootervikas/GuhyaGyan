package com.vikas.guhyagyan.utils

import android.content.Context
import android.content.SharedPreferences

class LoginManager(val context: Context) {
    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    private val mode = 0

    init {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, mode)
        editor = sharedPreferences.edit()
    }

    fun removeSharedPreference() {
        editor.clear().apply()
    }


    fun setToken(tok: String) {
        editor.putString(TOKEN, tok).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN, "")
    }

    private var instance: LoginManager? = null

    fun getInstance(context: Context): LoginManager {
        if (instance == null) {
            instance = LoginManager(context)
        }
        return instance!!
    }

    companion object {
        private const val PREFERENCE_NAME = "loginManager"
        private const val TOKEN = "token"
    }

}
