package com.project.converter.parse

import com.project.converter.model.Specifiers
import com.project.converter.util.Regexes

// This class extracts total specifiers from market name.
class TotalSpecifierExtractor : SpecifierExtractor() {

    override fun getSpecifiers(name: String?): Specifiers {
        val matcher = Regexes.DECIMAL.toPattern().matcher(name!!)
        if (matcher.find()) {
            return Specifiers.of("total", matcher.group(0))
        }

        return Specifiers.empty()
    }
}
