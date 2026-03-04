package com.vikas.guhyagyan.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class UriToFile(context: Context) {

    private val applicationContext = context.applicationContext
    fun getImageBody(imageUri: Uri?): File? {
        val parcelFileDescriptor = imageUri?.let {
            applicationContext.contentResolver.openFileDescriptor(
                it,
                "r",
                null
            )
        }
        val file = imageUri?.let { applicationContext.contentResolver.getFileName(it) }?.let {
            File(
                applicationContext.cacheDir,
                it
            )
        }
        val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        return file
    }

    fun ContentResolver.getFileName(uri: Uri): String {
        var name = ""
        val cursor = query(
            uri, null, null,
            null, null
        )
        cursor?.use {
            it.moveToFirst()
            name = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
        return name
    }
}