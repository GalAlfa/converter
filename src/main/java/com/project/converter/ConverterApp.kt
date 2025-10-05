package com.project.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.project.converter.model.InMarket
import com.project.converter.model.OutMarket
import com.project.converter.service.MarketConversionService
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.nio.file.Files


// Main application class for converting market data from input JSON to output JSON
fun main(args: Array<String>) {
    try {
        runApp(args)
    } catch (e: Exception) {
        System.err.println("ERROR: ${e.message}")
        e.printStackTrace(System.err)
        System.err.println("\nUsage: converter.ConverterApp <input.json> <output.json> [--pretty]")
        kotlin.system.exitProcess(1)
    }
}

private fun runApp(args: Array<String>) {
    require(args.size in 2..3) {
        "Expected 2 or 3 args: <input.json> <output.json> [--pretty]"
    }

    val inputPath = args[0]
    val outputPath = args[1]
    val pretty = args.size == 3 && args[2] == "--pretty"

    // Validate input/output paths
    val inFile = File(inputPath)
    require(inFile.isFile) { "Input file not found: $inputPath" }

    val outFile = File(outputPath)
    outFile.parentFile?.let { Files.createDirectories(it.toPath()) }

    // Configure JSON mapper
    val mapper = ObjectMapper()
        .registerModule(KotlinModule.Builder().build())
        .apply {
            propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
            if (pretty) enable(SerializationFeature.INDENT_OUTPUT)
        }

    // Read input JSON
    val inMarkets: List<InMarket> =
        mapper.readValue(inFile, object : TypeReference<List<InMarket>>() {})

    // Convert markets
    val service = MarketConversionService()
    val outMarkets: List<OutMarket> = service.convertAll(inMarkets)

    // Write output JSON
    mapper.writeValue(outFile, outMarkets)
    println("Conversion successful! Output written to: $outputPath")
}
