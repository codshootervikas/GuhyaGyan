package com.vikas.guhyagyan.models.register

data class RegistrationResponse(
    val `data`: Data?,
    val message: String?,
    val statusCode: Int?,
    val success: Boolean?
)