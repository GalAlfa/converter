package com.project.converter.model

// This class represents an output market object.
data class OutMarket(
    var marketUid: String? = null,
    var marketTypeId: String? = null,
    var specifiers: Specifiers? = null,
    var selections: MutableList<OutSelection>? = null,
)

