/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 17/05/2024 14:05
 * You are not allowed to remove the copyright.
 */
package plugin.postman_client_generator.companion

fun String.removeNonAlphaNumeric(): String {
    val regex = Regex("""[^A-Za-z0-9]""")
    return regex.replace(this, "")
}

val booleanPattern = Regex("""^(true|false)$""")

fun isBoolean(
    clues: List<String?>
): Boolean {
    return clues.filterNotNull()
        .fold(true) { acc, v -> acc && booleanPattern.matches(v) }
}

val numberPattern = Regex("^-?(0|[1-9]\\d*)(\\.\\d+)?\$")

fun isNumber(
    clues: List<String?>
): Boolean {
    return clues.filterNotNull()
        .fold(true) { acc, v -> acc && numberPattern.matches(v) }
}

val objectPattern = Regex("""^\{.*}$""")

fun isObject(
    clues: List<String?>
): Boolean {
    return clues.filterNotNull()
        .fold(true) { acc, v -> acc && objectPattern.matches(v) }
}

val arrayPattern = Regex("""^\s*\[.*]\s*$""")

fun isList(
    clues: List<String?>
): Boolean {
    return clues.filterNotNull()
        .fold(true) { acc, v -> acc && arrayPattern.matches(v) }
}