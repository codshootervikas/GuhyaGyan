package com.vikas.guhyagyan.models

data class ErrorResponse(
    val code: Int,
    val message: String,
    val resCode: Int,
    val success: Boolean
)