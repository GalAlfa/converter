package com.project.converter.model

// This class represents an input market object.
data class InMarket(
    var name: String,
    var eventId: String,
    var selections: List<InSelection>
)
