package com.project.converter.parse

import com.project.converter.model.InMarket
import com.project.converter.model.Specifiers


// Abstract class for extracting specifiers from an InMarket.
abstract class SpecifierExtractor {

    // extracts specifier from the given market
    open fun extract(market: InMarket?): Specifiers{
        val selections = market?.selections ?: return Specifiers.empty()

        for (selection in selections) {
            val name = selection.name.orEmpty().lowercase()
            return getSpecifiers(name)
        }

        return Specifiers.empty()
    }

    protected abstract fun getSpecifiers(name: String?): Specifiers
}
