/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 17/05/2024 14:05
 * You are not allowed to remove the copyright.
 */
package plugin.postman_client_generator.companion

import java.io.File

/**
 * scan for all file in directory including sub directory
 */
fun File.find(
    clue: String
): Sequence<File> {
    return listFiles()
        ?.asSequence()
        ?.mapNotNull { file ->
            when {
                !file.isDirectory && file.name.contains(clue) -> sequenceOf(file)
                file.isDirectory -> file.find(clue)
                else -> null
            }
        }
        ?.flatten()
        ?: sequenceOf()
}

fun printToFile(
    outputDir: File,
    fileName: String,
    content: String,
) : File {
    return File(
        outputDir,
        fileName
    )
        .apply {
            parentFile.mkdirs()
            writeText(
                content
            )
        }
}