/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.postman_client_generator.companion

import java.io.File

/**
 * scan for all file in directory including sub directory
 */
fun find(
    targetDir: File,
    clue: String
): Sequence<File> {
    return targetDir.listFiles()
        ?.asSequence()
        ?.mapNotNull { file ->
            when {
                !file.isDirectory && file.name.contains(clue) -> sequenceOf(file)
                file.isDirectory -> find(
                    file,
                    clue
                )

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
): File {
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