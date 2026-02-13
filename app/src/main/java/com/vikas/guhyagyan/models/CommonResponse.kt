package com.vikas.guhyagyan.models

data class CommonResponse(
    val code: Int,
    val `data`: String?,
    val message: String,
    val success: Boolean
)