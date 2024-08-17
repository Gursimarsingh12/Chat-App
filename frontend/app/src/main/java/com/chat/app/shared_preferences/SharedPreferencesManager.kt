package com.chat.app.shared_preferences

import android.content.SharedPreferences
import com.chat.app.users.domain.models.User
import com.google.gson.Gson

class SharedPreferencesManager(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER = "user"
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setUser(user: User) {
        val userJson = gson.toJson(user)
        sharedPreferences.edit().putString(KEY_USER, userJson).apply()
    }

    fun getUser(): User? {
        val userJson = sharedPreferences.getString(KEY_USER, null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    fun clearUser() {
        sharedPreferences.edit().remove(KEY_USER).apply()
    }
}


