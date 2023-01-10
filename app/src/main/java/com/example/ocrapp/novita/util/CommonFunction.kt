package com.example.ocrapp.novita.util

import android.content.Context
import com.snatik.storage.Storage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object CommonFunction {
    fun getOutputDirectory(context: Context): File {
        val storage = Storage(context)
        val mediaDir = storage.internalCacheDirectory?.let {
            File(it, Constant.FILE_DIR_CHILD).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }

    @Suppress("SameParameterValue")
    fun createFile(baseFolder: File, format: String, extension: String) =
        File(
            baseFolder,
            SimpleDateFormat(format, Locale.US).format(System.currentTimeMillis()) + extension
        )
}