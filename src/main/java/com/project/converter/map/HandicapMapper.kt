package com.project.converter.map

import com.project.converter.enums.SelectionType
import com.project.converter.model.InSelection
import com.project.converter.model.OutSelection
import com.project.converter.parse.HandicapSpecifierExtractor
import com.project.converter.util.TextUtils


// This class maps handicap markets from input to output format
class HandicapMapper(marketTypeId: String) : MarketMapper(
    marketTypeId,
    HandicapSpecifierExtractor()
) {

    override fun mapSelection(inSelection: InSelection, marketUid: String): OutSelection {
        val selectionName = TextUtils.toTrimmedAndLowercase(inSelection.name)
        val id = when {
            selectionName.startsWith("team a") -> SelectionType.HCP_TEAM_A.id
            selectionName.startsWith("team b") -> SelectionType.HCP_TEAM_B.id
            else -> throw IllegalArgumentException("Unknown handicap selection name: $selectionName")
        }

        return OutSelection().apply {
            selectionTypeId = id
            decimalOdds = inSelection.odds?.toDoubleOrNull() ?: 0.0
            selectionUid = "${marketUid}_$id"
        }
    }
}
