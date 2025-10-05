package com.project.converter.map

import com.project.converter.enums.SelectionType
import com.project.converter.model.InSelection
import com.project.converter.model.OutSelection
import com.project.converter.parse.TotalSpecifierExtractor
import com.project.converter.util.TextUtils

// This class maps total markets from input to output format
class TotalMapper(marketTypeId: String) : MarketMapper(
    marketTypeId,
    TotalSpecifierExtractor()
) {

    override fun mapSelection(inSelection: InSelection, marketUid: String): OutSelection {
        val selectionName = TextUtils.toTrimmedAndLowercase(inSelection.name)
        val id = when {
            selectionName.startsWith("over") -> SelectionType.OVER.id
            selectionName.startsWith("under") -> SelectionType.UNDER.id
            else -> throw IllegalArgumentException("Unknown total selection name: $selectionName")
        }

        return OutSelection().apply {
            selectionTypeId = id
            decimalOdds = inSelection.odds?.toDoubleOrNull() ?: 0.0
            selectionUid = "${marketUid}_$id"
        }
    }
}
