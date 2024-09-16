package com.wodo.gymapp.model

import java.util.Date

data class User(
    val username: String = "",
    val age: Int = 0,
    val gender: String = "",
    val email: String = "",
    val password: String = "",
    val status: String = "approved",
    val create_on: Date = Date()
)
