package com.vikas.guhyagyan.models.verify_otp

data class VerifyOtpRequest(
    val email: String,
    val otp: String,
    val verifyKey: String
)