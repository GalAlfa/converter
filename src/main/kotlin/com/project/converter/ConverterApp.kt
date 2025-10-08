package com.project.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.project.converter.model.InMarket
import com.project.converter.model.OutMarket
import com.project.converter.service.MarketConversionService
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file


class ConverterApp : CliktCommand() {
    val commandHelp = "Convert market data from input JSON to output JSON format"

    // Input file argument (first argument)
    private val inputFile by argument(
        name = "input",
        help = "Input JSON file path"
    ).file(mustExist = true, canBeDir = false, mustBeReadable = true)

    // Output file argument (second argument)
    private val outputFile by argument(
        name = "output",
        help = "Output JSON file path"
    ).file(canBeDir = false)

    // Pretty argument (third argument - optional)
    private val pretty by option(
        "--pretty", "-p",
        help = "Pretty print the output JSON with indentation"
    ).flag(default = false)

    override fun run() {
        // Ensure output directory exists
        outputFile.parentFile?.mkdirs()

        // Configure JSON mapper
        val mapper = ObjectMapper()
            .registerModule(KotlinModule.Builder().build())
            .apply {
                propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
                if (pretty) enable(SerializationFeature.INDENT_OUTPUT)
            }

        // Read input JSON
        val inMarkets: List<InMarket> =
            mapper.readValue(inputFile, object : TypeReference<List<InMarket>>() {})

        // Convert markets
        val service = MarketConversionService()
        val outMarkets: List<OutMarket> = service.convertAll(inMarkets)

        // Write output JSON
        mapper.writeValue(outputFile, outMarkets)
        echo("Conversion successful! Output written to: ${outputFile.absolutePath}")
    }
}

fun main(args: Array<String>) = ConverterApp().main(args)