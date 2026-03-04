package com.vikas.guhyagyan.models.pdf

data class PDFResponse(
    val `data`: List<Data?>?,
    val message: String?,
    val statusCode: Int?,
    val success: Boolean?
)