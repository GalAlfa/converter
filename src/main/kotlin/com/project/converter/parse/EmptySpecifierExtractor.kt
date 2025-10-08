package com.project.converter.parse

import com.project.converter.model.Specifiers

// This class always returns empty specifiers.
// Meant for markets without specifiers.
class EmptySpecifierExtractor : SpecifierExtractor() {
    override fun getSpecifiers(name: String?): Specifiers = Specifiers.empty()
}
