package com.singularityindonesia.storage

import android.content.Context
import android.media.MediaScannerConnection
import java.io.File

fun notifyMediaScanner(
    context: Context,
    file: File,
    onCompleteListener: MediaScannerConnection.OnScanCompletedListener
) {
    MediaScannerConnection
        .scanFile(
            context,
            arrayOf(file.absolutePath),
            null,
            onCompleteListener
        )
}