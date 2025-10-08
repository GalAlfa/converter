package com.project.converter.model

// This class represents an output market object.
data class OutMarket(
    var marketUid: String,
    var marketTypeId: Int,
    var specifiers: Specifiers,
    var selections: List<OutSelection>,
)

