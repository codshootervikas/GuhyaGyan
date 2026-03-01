package com.vikas.guhyagyan.models.login

data class LoginResponse(
    val `data`: Data?,
    val message: String?,
    val statusCode: Int?,
    val success: Boolean?
)