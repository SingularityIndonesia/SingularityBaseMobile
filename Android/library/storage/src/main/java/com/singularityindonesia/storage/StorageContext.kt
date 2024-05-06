package com.singularityindonesia.storage

import android.content.Context
import android.media.MediaScannerConnection
import com.singularityindonesia.main_context.StorageContext
import java.io.File

fun storageContext(
    context: Context
): Lazy<StorageContext> {
    return lazy {
        object : StorageContext {
            override fun getOutputDirectory(
                folderName: String
            ): File {
                return getOutputDirectory(folderName)
            }

            override fun notifyMediaScanner(
                file: File,
                onCompleteListener: (path: String, pathFromUri: String?) -> Unit
            ) {
                MediaScannerConnection
                    .scanFile(
                        context,
                        arrayOf(file.absolutePath),
                        null
                    ) { path, uri ->
                        onCompleteListener(path, uri.path)
                    }
            }
        }
    }
}