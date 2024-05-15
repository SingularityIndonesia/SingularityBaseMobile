package plugin.convention.companion

fun String.removeNonAlphaNumeric(): String {
    val regex = Regex("""[^A-Za-z0-9]""")
    return regex.replace(this, "")
}

val booleanPattern = Regex("""^(true|false)$""")

fun isBoolean(
    clue: String
): Boolean {
    return booleanPattern.matches(clue)
}

val numberPattern = Regex("""^-?(0|[1-9]\d*)(\.\d+)?([eE][+-]?\d+)?$""")

fun isNumber(
    clue: String
): Boolean {
    return numberPattern.matches(clue)
}

val objectPattern = Regex("""^\{.*}$""")

fun isObject(
    clue: String
): Boolean {
    return objectPattern.matches(clue)
}

val arrayPattern = Regex("""^\[.*\]$""")

fun isList(
    clue: String
): Boolean {
    return arrayPattern.matches(clue)
}