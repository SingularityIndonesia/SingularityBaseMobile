package com.singularityindonesia.storage

import android.content.Context
import java.io.File

fun getOutputDirectory(
    context: Context,
    folderName: String = "SingularityIndonesia"
): File {
    val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
        File(it, folderName).apply { mkdirs() }
    }

    return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
}