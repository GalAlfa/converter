package com.project.converter.util

// This object builds unique identifiers for markets and selections
object UidBuilder {
    fun buildMarketUid(eventId: String, marketTypeId: Int, specifier: String?): String {
        val spec = specifier.notNull().trim().takeUnless { it.isBlank() }.orEmpty()
        return if (spec.isNotEmpty()) {
            "${eventId}_${marketTypeId}_${spec}"
        } else {
            "${eventId}_${marketTypeId}"
        }
    }
}
