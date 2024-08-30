package com.wodo.gymapp.utils

object ValidationUtils {

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    fun isAgeValid(age: String): Boolean {
        return age.toIntOrNull()?.let { it > 0 } ?: false
    }

    fun isUsernameValid(username: String): Boolean {
        return username.isNotEmpty()
    }
}
