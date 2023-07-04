package com.example.data

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import javax.inject.Inject

class FileFromUriResolverImpl @Inject constructor(private val context: Context): FileFromUriResolver {
    override fun parseFileFromUri(uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        filePath?.let { return File(it) }
        return null
    }

    override fun getFileType(uri: Uri): String? {
        return context.contentResolver.getType(uri)
    }
}