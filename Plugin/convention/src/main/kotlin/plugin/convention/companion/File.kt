package plugin.convention.companion

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