package net.prxnxt.aamvaparser

import kotlin.math.round

/**
 * Utility functions
 */

internal object Utils {
    private const val CENTIMETERS_TO_INCHES_FACTOR = 0.393701
    private const val KILOGRAMS_TO_POUNDS_FACTOR = 2.20462

    fun inchesFromCentimeters(centimeters: Double): Double =
        round(CENTIMETERS_TO_INCHES_FACTOR * centimeters)

    fun poundsFromKilograms(kilograms: Double): Double =
        round(KILOGRAMS_TO_POUNDS_FACTOR * kilograms)
}