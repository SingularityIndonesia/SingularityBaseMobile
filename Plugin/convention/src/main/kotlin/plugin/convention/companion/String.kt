package plugin.convention.companion

fun String.removeNonAlphaNumeric(): String {
    val regex = Regex("""[^A-Za-z0-9]""")
    return regex.replace(this, "")
}