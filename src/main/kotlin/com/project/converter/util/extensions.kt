package com.project.converter.util

// This file contains extension functions for this project
fun String?.toTrimmedAndLowercase(): String = this?.trim()?.lowercase().orEmpty()