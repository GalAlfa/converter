package com.project.converter.util

// This object contains regex patterns for matching numbers and signed numbers
object Regexes {

    // regex patterns to match numbers
    val DECIMAL = Regex("""\d+(?:\.\d+)?""")

    // regex pattern to match signed numbers (positive or negative)
    val SIGNED_DECIMAL = Regex("""[+-]?\d+(?:\.\d+)?""")
}
