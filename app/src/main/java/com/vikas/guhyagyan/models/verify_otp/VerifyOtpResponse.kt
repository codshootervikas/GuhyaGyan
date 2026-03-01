package com.vikas.guhyagyan.models.verify_otp

data class VerifyOtpResponse(
    val `data`: Data?,
    val message: String?,
    val statusCode: Int?,
    val success: Boolean?
)