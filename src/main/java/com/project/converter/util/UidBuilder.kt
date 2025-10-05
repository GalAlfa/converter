package com.project.converter.util

// This object builds unique identifiers for markets and selections
object UidBuilder {
    fun buildMarketUid(eventId: String, marketTypeId: String, specifier: String?): String {
        val spec = specifier?.trim().takeUnless { it.isNullOrBlank() }.orEmpty()
        return listOf(eventId, marketTypeId, spec)
            .filter { it.isNotEmpty() }
            .joinToString("_")
    }

    fun buildSelectionUid(marketUid: String, selectionTypeId: Int): String =
        "${marketUid}_$selectionTypeId"
}
