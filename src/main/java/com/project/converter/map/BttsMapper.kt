package com.project.converter.map

import com.project.converter.enums.MarketType
import com.project.converter.enums.SelectionType
import com.project.converter.model.*
import com.project.converter.parse.EmptySpecifierExtractor
import com.project.converter.util.TextUtils
import com.project.converter.util.TextUtils.toTrimmedAndLowercase
import com.project.converter.util.UidBuilder.buildMarketUid
import com.project.converter.util.UidBuilder.buildSelectionUid

// This class maps "Both Teams To Score" (BTTS) markets from input to output format
class BttsMapper : MarketMapper(
    MarketType.BTTS.id,
    EmptySpecifierExtractor()
) {

    override fun mapSelection(inSelection: InSelection, marketUid: String): OutSelection {
        val id = when (val selectionName = TextUtils.toTrimmedAndLowercase(inSelection.name)) {
            "yes" -> SelectionType.YES.id
            "no" -> SelectionType.NO.id
            else -> throw IllegalArgumentException("Unknown BTTS selection name: $selectionName")
        }

        return OutSelection().apply {
            selectionTypeId = id
            decimalOdds = inSelection.odds?.toDoubleOrNull() ?: 0.0
            selectionUid = "${marketUid}_$id"
        }
    }
}
