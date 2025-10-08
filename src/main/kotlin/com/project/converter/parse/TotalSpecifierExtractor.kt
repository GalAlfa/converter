package com.project.converter.parse

import com.project.converter.model.Specifiers
import com.project.converter.util.Regexes
import com.project.converter.util.notNull

// This class extracts total specifiers from market name.
class TotalSpecifierExtractor : SpecifierExtractor() {

    override fun getSpecifiers(name: String?): Specifiers {
        val matcher = Regexes.DECIMAL.toPattern().matcher(name.notNull())
        if (matcher.find()) {
            return Specifiers.of("total", matcher.group(0))
        }

        return Specifiers.empty()
    }
}
