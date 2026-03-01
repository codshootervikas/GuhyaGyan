package com.vikas.guhyagyan.models.login

data class LoginRequest(
    val email: String,
    val password: String,
    val phone: String
)