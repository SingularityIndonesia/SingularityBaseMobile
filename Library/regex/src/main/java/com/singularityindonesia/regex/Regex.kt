package com.singularityindonesia.regex

val EmailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$\n".toRegex()
val NotEmptyRegex = "^.+\$".toRegex()