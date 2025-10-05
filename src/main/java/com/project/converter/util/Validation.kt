package com.project.converter.util

// This object provides validation utilities
object Validation {

    fun require(condition: Boolean, message: String) {
        if (!condition) throw IllegalArgumentException(message)
    }

    fun <T> requireNonNull(value: T?, message: String): T {
        return value ?: throw IllegalArgumentException(message)
    }
}
