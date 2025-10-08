package com.project.converter.parse

import com.project.converter.model.Specifiers
import com.project.converter.util.Regexes

// This class extracts handicap specifiers from market selections.
class HandicapSpecifierExtractor : SpecifierExtractor() {

    override fun getSpecifiers(name: String?): Specifiers {
        val matcher = Regexes.SIGNED_DECIMAL.toPattern().matcher(name!!)
        if (matcher.find()) {
            // Take numeric part without the leading sign
            return Specifiers.of("hcp", matcher.group(0).substring(1))
        }

        return Specifiers.empty()
    }
}
