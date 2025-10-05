package com.project.converter.parse

import com.project.converter.model.InMarket
import com.project.converter.model.Specifiers
import com.project.converter.util.TextUtils

// This class always returns empty specifiers.
// Meant for markets without specifiers.
class EmptySpecifierExtractor : SpecifierExtractor() {

    override fun extract(market: InMarket?): Specifiers{
        return getSpecifiers("")
    }

    override fun getSpecifiers(name: String?): Specifiers = Specifiers.empty()
}
