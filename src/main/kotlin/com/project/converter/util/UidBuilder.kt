package com.project.converter.util

// This object builds unique identifiers for markets and selections
object UidBuilder {
    fun buildMarketUid(eventId: String, marketTypeId: Int, specifier: String?): String {
        val spec = specifier?.trim().takeUnless { it.isNullOrBlank() }.orEmpty()
        return if (spec.isNotEmpty()) {
            "${eventId}_${marketTypeId}_${spec}"
        } else {
            "${eventId}_${marketTypeId}"
        }
    }

    fun buildSelectionUid(marketUid: String, selectionTypeId: Int): String =
        "${marketUid}_$selectionTypeId"
}
