package com.singularityindonesia.main_context

import java.io.File

interface StorageContext {
    fun getOutputDirectory(
        folderName: String = "SingularityIndonesia"
    ): File

    fun notifyMediaScanner(
        file: File,
        onCompleteListener: (path: String, pathFromUri: String?) -> Unit
    )
}