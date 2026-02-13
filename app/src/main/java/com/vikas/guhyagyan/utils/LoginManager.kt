package com.vikas.guhyagyan.utils

import android.content.Context
import android.content.SharedPreferences

class LoginManager(context: Context) {

    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    private val mode = 0

    init {
        sharedPreferences = context.getSharedPreferences("guhyagyan", mode)
        editor = sharedPreferences.edit()
    }

    fun getToken(): String {
        return sharedPreferences.getString(token, "") ?: ""
    }

    fun setToken(token: String) {
        editor.putString(this.token, token)
    }

    fun removeSharedPreference() {
        editor.clear().apply()
    }

    private var instance: LoginManager? = null
    fun getInstance(context: Context): LoginManager {
        if (instance == null) {
            instance = LoginManager(context)
        }
        return instance!!
    }

    private val token = "authToken"

}
