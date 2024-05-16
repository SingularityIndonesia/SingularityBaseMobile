package plugin.convention.companion

import org.gradle.internal.impldep.org.junit.Test

fun String.removeNonAlphaNumeric(): String {
    val regex = Regex("""[^A-Za-z0-9]""")
    return regex.replace(this, "")
}

val booleanPattern = Regex("""^(true|false)$""")

fun isBoolean(
    clue: String?
): Boolean {
    return booleanPattern.matches(clue.toString())
}

val numberPattern = Regex("""^-?(0|[1-9]\d*)(\.\d+)?([eE][+-]?\d+)?$""")

fun isNumber(
    clue: String?
): Boolean {
    return numberPattern.matches(clue.toString())
}

val objectPattern = Regex("""^\{.*}$""")

fun isObject(
    clue: String?
): Boolean {
    return objectPattern.matches(clue.toString())
}

val arrayPattern = Regex("""^\s*\[.*]\s*$""")

fun isList(
    clue: String?
): Boolean {
    return arrayPattern.matches(clue.toString())
}