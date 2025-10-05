package com.project.converter.model

// This class represents an output selection object.
data class OutSelection(
    var selectionUid: String? = null,
    var selectionTypeId: Int = 0,
    var decimalOdds: Double = 0.0
)