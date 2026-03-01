package com.vikas.guhyagyan.models.register

data class RegisterRequest(
    val email: String?,
    val name: String?,
    val password: String?,
    val phone: String?,
    val image: String?
)