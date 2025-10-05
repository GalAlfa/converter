package com.project.converter.model

// This class represents an input market object.
data class InMarket(
    var name: String? = null,
    var eventId: String? = null,
    var selections: MutableList<InSelection>? = null
)
