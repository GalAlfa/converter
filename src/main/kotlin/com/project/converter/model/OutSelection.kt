package com.project.converter.model

// This class represents an output selection object.
data class OutSelection(
    var selectionUid: String,
    var selectionTypeId: Int,
    var decimalOdds: Double
)