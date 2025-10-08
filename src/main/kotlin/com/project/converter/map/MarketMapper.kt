package com.project.converter.map

import com.project.converter.model.InMarket
import com.project.converter.model.InSelection
import com.project.converter.model.OutMarket
import com.project.converter.model.OutSelection
import com.project.converter.parse.SpecifierExtractor
import com.project.converter.util.UidBuilder

// Abstract  class for mapping InMarket objects to OutMarket objects.
// Handles the shared mapping logic for all market types.
abstract class MarketMapper(
    private val marketTypeId: Int,
    private val specifierExtractor: SpecifierExtractor
) {


    // Converts an InMarket into an OutMarket.
    fun map(inMarket: InMarket): OutMarket {
        val specifiers = specifierExtractor.extract(inMarket)
        val specValue = specifiers.specifiers.values.firstOrNull().orEmpty()

        val marketUid = UidBuilder.buildMarketUid(
            inMarket.eventId.trim(),
            marketTypeId,
            specValue
        )

        val outSelections = inMarket.selections.mapNotNull { inSelection ->
            mapSelection(inSelection, marketUid)
        }

        return OutMarket(
            marketUid = marketUid,
            marketTypeId = this@MarketMapper.marketTypeId,
            specifiers = specifiers,
            selections = outSelections.toList()
        )
    }


    // Each mapper defines how to transform a single InSelection.
    protected abstract fun mapSelection(inSelection: InSelection, marketUid: String): OutSelection?
}
