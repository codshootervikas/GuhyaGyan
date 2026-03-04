package com.vikas.guhyagyan.models.file

data class FileUploadResponse(
    val `data`: Data?,
    val message: String?,
    val statusCode: Int?,
    val success: Boolean?
)