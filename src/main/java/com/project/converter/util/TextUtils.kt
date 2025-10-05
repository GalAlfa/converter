package com.project.converter.util

// This object contains utility methods for text processing
object TextUtils {

    fun isBlank(s: String?): Boolean = s == null || s.trim().isEmpty()

    fun nullToEmpty(s: String?): String = s ?: ""

    fun toTrimmedAndLowercase(s: String?): String = s?.trim()?.lowercase() ?: ""
}
