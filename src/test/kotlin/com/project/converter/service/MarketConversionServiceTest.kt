package com.project.converter.service

import com.project.converter.model.InMarket
import com.project.converter.model.InSelection
import com.project.converter.model.OutMarket
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

// Unit tests for MarketConversionService
class MarketConversionServiceTest {

    private val marketConversionService = MarketConversionService()

    private fun getInSelection(name: String, odds: Double): InSelection {
        val inSelection = InSelection(
            name = name,
            odds = odds.toString()
        )
        return inSelection
    }

    @DisplayName("Test Conversion of OneXTwo Market")
    @Test
    fun testOneXTwo() {
        val inMarket = InMarket(
            eventId = "e1",
            name = "1x2",
            selections = mutableListOf(
                getInSelection("Team A", 1.65),
                getInSelection("draw", 3.2),
                getInSelection("Team B", 2.6)
            )
        )

        val outMarket: OutMarket = marketConversionService.convert(inMarket)

        assertAll(
            { assertEquals(1, outMarket.marketTypeId,
                "Market type ID should be '1' for 1X2 market") },
            { assertEquals("e1_1", outMarket.marketUid,
                "Market UID should be correctly formatted. Expected 'e1_1'") },
            { assertEquals(3, outMarket.selections.size,
                "There should be 3 selections in the output market") }
        )
    }

    @DisplayName("Test Conversion of Total Market")
    @Test
    fun testTotal() {
        val inMarket = InMarket(
            eventId = "e2",
            name = "Total",
            selections = mutableListOf(
                getInSelection("over 2.5", 1.85),
                getInSelection("under 2.5", 1.95)
            )
        )

        val outMarket = marketConversionService.convert(inMarket)

        assertAll(
            { assertEquals(18, outMarket.marketTypeId, "Market type ID should be '18' for Total market") },
            { assertEquals("e2_18_2.5", outMarket.marketUid, "Market UID should be correctly formatted. Expected 'e2_18_2.5'") },
            { assertEquals(2, outMarket.selections.size, "There should be 2 selections in the output market") }
        )
    }

    @DisplayName("Test Conversion of Handicap Market")
    @Test
    fun testHandicap() {
        val inMarket = InMarket(
            eventId = "e3",
            name = "Handicap",
            selections = mutableListOf(
                getInSelection("Team A +1.5", 1.8),
                getInSelection("Team B -1.5", 2.0)
            )
        )

        val outMarket = marketConversionService.convert(inMarket)

        assertAll(
            { assertEquals(16, outMarket.marketTypeId, "Market type ID should be '16' for Handicap market") },
            { assertEquals("e3_16_1.5", outMarket.marketUid, "Market UID should be correctly formatted. Expected 'e3_16_1.5'") },
            { assertEquals(2, outMarket.selections.size, "There should be 2 selections in the output market") }
        )
    }

    @DisplayName("Test Conversion of Both Teams to Score Market")
    @Test
    fun testBtts() {
        val inMarket = InMarket(
            eventId = "e4",
            name = "Both teams to score",
            selections = mutableListOf(
                getInSelection("Yes", 1.7),
                getInSelection("No", 2.1)
            )
        )

        val outMarket = marketConversionService.convert(inMarket)

        assertAll(
            { assertEquals(50, outMarket.marketTypeId, "Market type ID should be '50' for BTTS market") },
            { assertEquals("e4_50", outMarket.marketUid, "Market UID should be correctly formatted. Expected 'e4_50'") },
            { assertEquals(2, outMarket.selections.size, "There should be 2 selections in the output market") }
        )
    }

    @DisplayName("OneXTwo Case Insensitive and Spaces")
    @Test
    fun testOneXTwoInsensitiveCaseAndSpaces() {
        val inMarket = InMarket(
            eventId = " e5",
            name = "  1X2  ",
            selections = mutableListOf(
                getInSelection("  TEAM a ", 1.1),
                getInSelection(" Draw", 3.0),
                getInSelection("team B  ", 5.0)
            )
        )

        val outMarket = marketConversionService.convert(inMarket)

        assertAll(
            { assertEquals(1, outMarket.marketTypeId,
                "Market type ID should be '1' for 1X2 market") },
            { assertEquals("e5_1", outMarket.marketUid,
                "Market UID should be correctly formatted. Expected 'e5_1'") },
            { assertEquals(3, outMarket.selections.size,
                "There should be 3 selections in the output market") },
            { assertEquals(1, outMarket.selections[0].selectionTypeId,
                "Selection type ID for Team A should be 1") },
            { assertEquals(2, outMarket.selections[1].selectionTypeId,
                "Selection type ID for Draw should be 2") },
            { assertEquals(3, outMarket.selections[2].selectionTypeId,
                "Selection type ID for Team B should be 3") }
        )
    }

    @DisplayName("Test Support for Integer Specifier in Total Market")
    @Test
    fun testSupportForIntegerSpecifier() {
        val inMarket = InMarket(
            eventId = "e6",
            name = "Total",
            selections = mutableListOf(
                getInSelection("over 2", 1.8),
                getInSelection("under 2", 2.0)
            )
        )

        val outMarket = marketConversionService.convert(inMarket)

        assertEquals(18, outMarket.marketTypeId, "Market type ID should be '18' for Total market")
        assertEquals("e6_18_2", outMarket.marketUid, "Market UID should be correctly formatted. Expected 'e6_18_2'")
    }

    @DisplayName("Test Conversion of 1st Half Total Market")
    @Test
    fun testTotalFirstHalfTypeId() {
        val inMarket = InMarket(
            eventId = "e11",
            name = "1st half - total",
            selections = mutableListOf(
                getInSelection("over 1.5", 2.1),
                getInSelection("under 1.5", 1.7)
            )
        )

        val outMarket = marketConversionService.convert(inMarket)

        assertAll(
            { assertEquals(68, outMarket.marketTypeId, "Market type ID should be '68' for 1st half Total market") },
            { assertEquals("e11_68_1.5", outMarket.marketUid, "Market UID should be correctly formatted. Expected 'e11_68_1.5'") }
        )
    }

    @DisplayName("Test Handicap Market Specifier with Leading Sign")
    @Test
    fun testSpecifierWithLeadingSign() {
        val inMarket = InMarket(
            eventId = "e12",
            name = "Handicap",
            selections = mutableListOf(
                getInSelection("Team A -1.5", 1.9),
                getInSelection("Team B +1.5", 1.9)
            )
        )

        val outMarket = marketConversionService.convert(inMarket)

        assertAll(
            { assertEquals(16, outMarket.marketTypeId, "Market type ID should be '16' for Handicap market") },
            { assertEquals("e12_16_1.5", outMarket.marketUid, "Market UID should be correctly formatted. Expected 'e12_16_1.5'") },
            { assertEquals(1714, outMarket.selections[0].selectionTypeId, "Selection type ID for Team A should be 1714") },
            { assertEquals(1715, outMarket.selections[1].selectionTypeId, "Selection type ID for Team B should be 1715") }
        )
    }

    @DisplayName("Test First and Second Half Handicap Markets")
    @Test
    fun testFirstAndSecondHalfHandicap() {
        val inMarket1 = InMarket("1st half - handicap", "e13",
            mutableListOf(
            getInSelection("Team A +0.5", 1.9),
            getInSelection("Team B -0.5", 1.9)
        ))

        val inMarket2 = InMarket("2nd half - handicap", "e14",
            mutableListOf(
            getInSelection("Team A +1", 2.0),
            getInSelection("Team B -1", 1.8)
        ))

        val outMarket1 = marketConversionService.convert(inMarket1)
        val outMarket2 = marketConversionService.convert(inMarket2)

        assertAll(
            { assertEquals(66, outMarket1.marketTypeId,
                "Market type ID should be '66' for 1st half Handicap market") },
            { assertEquals("e13_66_0.5", outMarket1.marketUid,
                "Market UID should be correctly formatted. Expected 'e13_66_0.5'") },
            { assertEquals(88, outMarket2.marketTypeId,
                "Market type ID should be '88' for 2nd half Handicap market") },
            { assertEquals("e14_88_1", outMarket2.marketUid,
                "Market UID should be correctly formatted. Expected 'e14_88_1'") }
        )
    }

    @DisplayName("Exception on Unknown Total Selection Name")
    @Test
    fun exceptionOnUnknownTotalSelectionName() {
        val inMarket = InMarket("Total", "e15", mutableListOf(
            getInSelection("notover 2.5", 1.8),
            getInSelection("under 2.5", 2.0)
        ))

        val exception = assertThrows(IllegalArgumentException::class.java) {
            marketConversionService.convert(inMarket)
        }

        assertEquals("Unknown total selection name: notover 2.5", exception.message)
    }

    @DisplayName("Exception on Unknown Handicap Selection Name")
    @Test
    fun exceptionOnUnknownHandicapSelectionName() {
        val inMarket = InMarket("Handicap", "e16", mutableListOf(
            getInSelection("Home +1.5", 1.8),
            getInSelection("Team B -1.5", 2.0)
        ))

        val exception = assertThrows(IllegalArgumentException::class.java) {
            marketConversionService.convert(inMarket)
        }

        assertEquals("Unknown handicap selection name: home +1.5", exception.message)
    }

    @DisplayName("Exception on Blank Market Name")
    @Test
    fun exceptionOnBlankMarketName() {
        val inMarket = InMarket("   ", "Total", mutableListOf(
            getInSelection("over 2.5", 1.8),
            getInSelection("under 2.5", 2.0)
        ))

        val exception = assertThrows(IllegalArgumentException::class.java) {
            marketConversionService.convert(inMarket)
        }

        assertEquals("Market name must not be blank", exception.message)
    }

    @DisplayName("Exception on Blank Event Id")
    @Test
    fun exceptionOnBlankEventId() {
        val inMarket = InMarket("Total", "   ", mutableListOf(
            getInSelection("over 2.5", 1.8),
            getInSelection("under 2.5", 2.0)
        ))

        val exception = assertThrows(IllegalArgumentException::class.java) {
            marketConversionService.convert(inMarket)
        }

        assertEquals("eventId must not be blank", exception.message)
    }

    @DisplayName("Exception on Empty Selections")
    @Test
    fun exceptionOnEmptySelections() {
        val inMarket = InMarket("1x2", "e17", mutableListOf())

        val exception = assertThrows(IllegalArgumentException::class.java) {
            marketConversionService.convert(inMarket)
        }

        assertEquals("Selections must not be empty", exception.message)
    }

    @DisplayName("Check if convertAll preserves order of markets")
    @Test
    fun checkIfConvertAllPreservesOrderOfMarkets() {
        val inMarket1 = InMarket("1x2", "e18", mutableListOf(
            getInSelection("Team A", 1.1),
            getInSelection("draw", 2.2),
            getInSelection("Team B", 3.3)
        ))

        val inMarket2 = InMarket("Total", "e19", mutableListOf(
            getInSelection("over 3.5", 2.1),
            getInSelection("under 3.5", 1.7)
        ))

        val outMarkets = marketConversionService.convertAll(mutableListOf(inMarket1, inMarket2))

        assertAll(
            { assertEquals(2, outMarkets.size, "There should be 2 converted markets") },
            { assertEquals("e18_1", outMarkets[0].marketUid, "First market UID should be 'e18_1'") },
            { assertEquals("e19_18_3.5", outMarkets[1].marketUid, "Second market UID should be 'e19_18_3.5'") }
        )
    }
}