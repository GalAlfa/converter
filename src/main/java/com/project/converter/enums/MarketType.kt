package com.project.converter.enums

// This enum represents different types of betting markets
enum class MarketType(val id: String, val displayName: String) {
    ONE_X_TWO("1", "1x2"),
    TOTAL("18", "Total"),
    FIRST_HALF_TOTAL("68", "1st half - total"),
    HANDICAP("16", "Handicap"),
    FIRST_HALF_HANDICAP("66", "1st half - handicap"),
    SECOND_HALF_HANDICAP("88", "2nd half - handicap"),
    BTTS("50", "Both teams to score");

    companion object {
        // gets id by name
        fun fromName(name: String): MarketType {
            val normalized = name.trim().lowercase()
            return entries.firstOrNull { it.displayName.lowercase() == normalized }
                ?: throw IllegalArgumentException("Unknown market name: $name")
        }
    }
}
