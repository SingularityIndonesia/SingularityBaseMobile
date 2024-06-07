package plugin.convention.companion

import java.io.File

class ReleaseNote(
    private val file: File,
) {

    val versionCode = file.useLines { lines ->
        lines
            .first {
                it.contains("versionCode")
            }
            .split("=")[1]
            .replace(" ", "")
            .toInt()
    }

    val versionName = file.useLines { lines ->
        lines
            .first {
                it.contains("# Version")
            }
            .split(" ")[2]
            .replace(" ", "")
    }
}