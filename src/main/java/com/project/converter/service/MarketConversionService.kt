package com.project.converter.service

import com.project.converter.enums.MarketType
import com.project.converter.map.BttsMapper
import com.project.converter.map.HandicapMapper
import com.project.converter.map.MarketMapper
import com.project.converter.map.OneXTwoMapper
import com.project.converter.map.TotalMapper
import com.project.converter.model.InMarket
import com.project.converter.model.OutMarket
import com.project.converter.util.TextUtils.isBlank
import com.project.converter.util.Validation

// This service handles conversion of input market data to output market data using the appropriate mappers
class MarketConversionService {

    // marketType -> MarketMapper
    private val mappers: Map<MarketType, MarketMapper> = mapOf(
        MarketType.ONE_X_TWO to OneXTwoMapper(),
        MarketType.BTTS to BttsMapper(),
        MarketType.HANDICAP to HandicapMapper(MarketType.HANDICAP.id),
        MarketType.FIRST_HALF_HANDICAP to HandicapMapper(MarketType.FIRST_HALF_HANDICAP.id),
        MarketType.SECOND_HALF_HANDICAP to HandicapMapper(MarketType.SECOND_HALF_HANDICAP.id),
        MarketType.TOTAL to TotalMapper(MarketType.TOTAL.id),
        MarketType.FIRST_HALF_TOTAL to TotalMapper(MarketType.FIRST_HALF_TOTAL.id)
    )

    // Converts a list of InMarket into a list of OutMarket.
    fun convertAll(inMarkets: List<InMarket>?): List<OutMarket> {
        Validation.requireNonNull(inMarkets, "Input market list cannot be null")

        return inMarkets!!.map { market ->
            convert(market)
        }
    }

    // Converts a single InMarket to OutMarket if supported.
    fun convert(inMarket: InMarket): OutMarket {
        Validation.require(!inMarket.name.isNullOrBlank(), "Market name must not be blank")
        Validation.require(!inMarket.eventId.isNullOrBlank(), "eventId must not be blank")
        Validation.require(!(inMarket.selections == null ||
                inMarket.selections!!.isEmpty()), "Selections must not be null or empty")

        val marketType = MarketType.fromName(inMarket.name!!)
        val mapper = mappers[marketType]
            ?: throw IllegalArgumentException("No mapper found for market type: $marketType")

        return mapper.map(inMarket)
    }
}
