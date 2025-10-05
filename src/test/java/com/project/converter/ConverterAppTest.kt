package com.project.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.project.converter.model.InMarket
import com.project.converter.model.OutMarket
import com.project.converter.service.MarketConversionService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

// End-to-end tests for the ConverterApp
class ConverterAppTest {

    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper()
        objectMapper.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
    }

    @DisplayName("Test Conversion of Full JSON Example")
    @Test
    fun testFullJsonExample() {
        val input = """[{"name":"1x2","event_id":"123456","selections":[
            {"name":"Team A","odds":1.65},
            {"name":"draw","odds":3.2},
            {"name":"Team B","odds":2.6}
        ]}]"""

        val inMarkets: List<InMarket> =
            objectMapper.readValue(input, object : TypeReference<List<InMarket>>() {})
        val service = MarketConversionService()
        val outMarkets: List<OutMarket> = service.convertAll(inMarkets)

        val outMarket = outMarkets[0]

        assertAll(
            { assertEquals("1", outMarket.marketTypeId, "Market type ID should be '1' for 1X2 market") },
            { assertEquals("123456_1", outMarket.marketUid, "Market UID should be correctly formatted") },
            { assertEquals(3, outMarket.selections?.size, "There should be 3 selections in the output market") }
        )
    }

    @DisplayName("Test Conversion of Total Market with Decimal Specifier")
    @Test
    fun testTotalDecimalSpecifier() {
        val input = """
            [{"name":"Total","event_id":"e2","selections":[
              {"name":"over 2.5","odds":1.85},
              {"name":"under 2.5","odds":1.95}
            ]}]
        """.trimIndent()

        val inMarkets: List<InMarket> = objectMapper.readValue(input, object : TypeReference<List<InMarket>>() {})
        val outMarket = MarketConversionService().convertAll(inMarkets)[0]

        assertAll(
            { assertEquals("18", outMarket.marketTypeId, "Market type ID should be '18' for Total market") },
            { assertEquals("e2_18_2.5", outMarket.marketUid, "Market UID should include decimal specifier 2.5") }
        )
    }

    @DisplayName("Test Conversion of Total Market with Integer Specifier")
    @Test
    fun testTotalIntegerSpecifier() {
        val input = """
            [{"name":"Total","event_id":"e3","selections":[
              {"name":"over 2","odds":1.9},
              {"name":"under 2","odds":1.9}
            ]}]
        """.trimIndent()

        val inMarkets: List<InMarket> = objectMapper.readValue(input, object : TypeReference<List<InMarket>>() {})
        val outMarket = MarketConversionService().convertAll(inMarkets)[0]

        assertEquals("e3_18_2", outMarket.marketUid, "Market UID should contain '2' without trailing '.0'")
    }

    @DisplayName("Test Conversion of 1st Half Total Market")
    @Test
    fun testTotalFirstHalf() {
        val input = """
            [{"name":"1st half - total","event_id":"e4","selections":[
              {"name":"over 1.5","odds":2.1},
              {"name":"under 1.5","odds":1.7}
            ]}]
        """.trimIndent()

        val inMarkets: List<InMarket> = objectMapper.readValue(input, object : TypeReference<List<InMarket>>() {})
        val outMarket = MarketConversionService().convertAll(inMarkets)[0]

        assertAll(
            { assertEquals("68", outMarket.marketTypeId, "Market type ID should be '68' for 1st half Total market") },
            { assertEquals("e4_68_1.5", outMarket.marketUid, "Market UID should include 1.5 as specifier") }
        )
    }

    @DisplayName("Exception on Unknown Total Selection Name")
    @Test
    fun exceptionOnUnknownTotalSelection() {
        val input = """
            [{"name":"Total","event_id":"e5","selections":[
              {"name":"notover 2.5","odds":1.8},
              {"name":"under 2.5","odds":2.0}
            ]}]
        """.trimIndent()

        val inMarkets: List<InMarket> = objectMapper.readValue(input, object : TypeReference<List<InMarket>>() {})
        val marketConversionService = MarketConversionService()

        val exception = assertThrows(IllegalArgumentException::class.java) {
            marketConversionService.convertAll(inMarkets)
        }

        assertEquals("Unknown total selection name: notover 2.5", exception.message)
    }

    @DisplayName("Test Conversion of Handicap Market with Absolute Specifier")
    @Test
    fun testHandicapSpecifierAbsolute() {
        val input = """
            [{"name":"Handicap","event_id":"e6","selections":[
              {"name":"Team A -1.5","odds":1.9},
              {"name":"Team B +1.5","odds":1.9}
            ]}]
        """.trimIndent()

        val outMarket = MarketConversionService()
            .convertAll(objectMapper.readValue(input, object : TypeReference<List<InMarket>>() {}))[0]

        assertAll(
            { assertEquals("16", outMarket.marketTypeId, "Market type ID should be '16' for Handicap market") },
            { assertEquals("e6_16_1.5", outMarket.marketUid, "Market UID should use absolute specifier 1.5") }
        )
    }

    @DisplayName("Test Conversion of First and Second Half Handicap Markets")
    @Test
    fun testHandicapHalfMarkets() {
        val input = """
            [
              {"name":"1st half - handicap","event_id":"e7","selections":[
                {"name":"Team A +0.5","odds":1.9},
                {"name":"Team B -0.5","odds":1.9}
              ]},
              {"name":"2nd half - handicap","event_id":"e8","selections":[
                {"name":"Team A +1","odds":2.0},
                {"name":"Team B -1","odds":1.8}
              ]}
            ]
        """.trimIndent()

        val outMarkets = MarketConversionService()
            .convertAll(objectMapper.readValue(
                input,
                object : TypeReference<List<InMarket>>() {})
            )

        assertAll(
            { assertEquals("66", outMarkets[0].marketTypeId) },
            { assertEquals("e7_66_0.5", outMarkets[0].marketUid) },
            { assertEquals("88", outMarkets[1].marketTypeId) },
            { assertEquals("e8_88_1", outMarkets[1].marketUid) }
        )
    }

    @DisplayName("Exception on Unknown Handicap Selection Name")
    @Test
    fun exceptionOnUnknownHandicapSelection() {
        val input = """
            [{"name":"Handicap","event_id":"e9","selections":[
              {"name":"Home +1.5","odds":1.8},
              {"name":"Team B -1.5","odds":2.0}
            ]}]
        """.trimIndent()

        val inMarkets = objectMapper.readValue(input, object : TypeReference<List<InMarket>>() {})
        val service = MarketConversionService()

        val exception = assertThrows(IllegalArgumentException::class.java) {
            service.convertAll(inMarkets)
        }

        assertEquals("Unknown handicap selection name: home +1.5", exception.message)
    }

    @DisplayName("Test Conversion of BTTS Market")
    @Test
    fun testBttsMarket() {
        val input = """
            [{"name":"Both teams to score","event_id":"e10","selections":[
              {"name":"Yes","odds":1.7},
              {"name":"No","odds":2.1}
            ]}]
        """.trimIndent()

        val outMarket = MarketConversionService()
            .convertAll(objectMapper.readValue(input, object : TypeReference<List<InMarket>>() {}))[0]

        assertAll(
            { assertEquals("50", outMarket.marketTypeId) },
            { assertEquals("e10_50", outMarket.marketUid) }
        )
    }

    @DisplayName("Case Insensitive Names and Order Preservation")
    @Test
    fun testCaseInsensitiveAndOrder() {
        val input = """
            [
              {"name":" 1X2 ","event_id":" e11 ","selections":[
                {"name":"  TEAM a ", "odds":1.1},
                {"name":" Draw",    "odds":3.0},
                {"name":"team B  ", "odds":5.0}
              ]},
              {"name":"Total","event_id":"e12","selections":[
                {"name":"over 3.5","odds":2.1},
                {"name":"under 3.5","odds":1.7}
              ]}
            ]
        """.trimIndent()

        val outMarkets = MarketConversionService()
            .convertAll(objectMapper.readValue(input, object : TypeReference<List<InMarket>>() {}))

        assertAll(
            { assertEquals("e11_1", outMarkets[0].marketUid) },
            { assertEquals("e12_18_3.5", outMarkets[1].marketUid) }
        )
    }

    @DisplayName("Exception on Blank Event Id")
    @Test
    fun exceptionOnBlankEventId() {
        val input = """
            [{"name":"Total","event_id":"   ","selections":[
              {"name":"over 2.5","odds":1.8},
              {"name":"under 2.5","odds":2.0}
            ]}]
        """.trimIndent()

        val inMarkets = objectMapper.readValue(input, object : TypeReference<List<InMarket>>() {})
        val service = MarketConversionService()

        val exception = assertThrows(IllegalArgumentException::class.java) {
            service.convertAll(inMarkets)
        }

        assertEquals("eventId must not be blank", exception.message)
    }

    @DisplayName("Exception on Blank Market Name")
    @Test
    fun exceptionOnBlankMarketName() {
        val input = """
            [{"name":"   ","event_id":"e99","selections":[
              {"name":"Yes","odds":1.7},
              {"name":"No","odds":2.1}
            ]}]
        """.trimIndent()

        val inMarkets = objectMapper.readValue(input, object : TypeReference<List<InMarket>>() {})
        val service = MarketConversionService()

        val exception = assertThrows(IllegalArgumentException::class.java) {
            service.convertAll(inMarkets)
        }

        assertEquals("Market name must not be blank", exception.message)
    }

    @DisplayName("Exception on Empty Selections")
    @Test
    fun exceptionOnEmptySelections() {
        val input = """[{"name":"1x2","event_id":"e13","selections":[]}]"""

        val inMarkets = objectMapper.readValue(input, object : TypeReference<List<InMarket>>() {})
        val service = MarketConversionService()

        val exception = assertThrows(IllegalArgumentException::class.java) {
            service.convertAll(inMarkets)
        }

        assertEquals("Selections must not be null or empty", exception.message)
    }

    @DisplayName("Test JSON Shape of Specifiers (Flat Object)")
    @Test
    fun testSpecifiersJsonShape() {
        val input = """
            [{"name":"Total","event_id":"e14","selections":[
              {"name":"over 2.5","odds":1.8},
              {"name":"under 2.5","odds":2.0}
            ]}]
        """.trimIndent()

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
        val outMarket = MarketConversionService()
            .convertAll(objectMapper.readValue(input, object : TypeReference<List<InMarket>>() {}))[0]

        val json = objectMapper.writeValueAsString(outMarket)

        assertAll(
            { assertTrue(json.contains("\"specifiers\" : {")) },
            { assertFalse(json.contains("\"specifiers\" : { \"specifiers\"")) }
        )
    }
}