package com.example.data

import android.net.Uri
import java.io.File

interface FileFromUriResolver {

    fun parseFileFromUri(uri: Uri): File?

    fun getFileType(uri: Uri): String?
}