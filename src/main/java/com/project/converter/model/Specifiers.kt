package com.project.converter.model

import com.fasterxml.jackson.annotation.JsonValue

// This class represents specifiers as a map of key-value pairs.

data class Specifiers(
    @get:JsonValue // ensures that during serialization only the map is the output
    val specifiers: Map<String, String> = emptyMap()
) {
    companion object {
        fun of(key: String, value: String): Specifiers =
            Specifiers(mapOf(key to value))

        fun empty(): Specifiers = Specifiers()
    }
}
