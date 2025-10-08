package com.project.converter.map

import com.project.converter.enums.MarketType
import com.project.converter.enums.SelectionType
import com.project.converter.model.*
import com.project.converter.parse.EmptySpecifierExtractor
import com.project.converter.util.toTrimmedAndLowercase


// This class maps "1X2" markets from input to output format
class OneXTwoMapper : MarketMapper(
    MarketType.ONE_X_TWO.id,
    EmptySpecifierExtractor()
) {

    override fun mapSelection(inSelection: InSelection, marketUid: String): OutSelection {
        val id = when (val selectionName = inSelection.name.toTrimmedAndLowercase()) {
            "team a" -> SelectionType.TEAM_A.id
            "draw" -> SelectionType.DRAW.id
            "team b" -> SelectionType.TEAM_B.id
            else -> throw IllegalArgumentException("Unknown 1X2 selection name: $selectionName")
        }

        return OutSelection(
            selectionTypeId = id,
            decimalOdds = inSelection.odds.toDoubleOrNull() ?: 0.0,
            selectionUid = "${marketUid}_$id"
        )
    }
}
