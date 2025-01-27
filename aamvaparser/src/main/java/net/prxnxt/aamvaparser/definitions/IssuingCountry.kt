package net.prxnxt.aamvaparser.definitions

/**
 * The AAMVA issuing country types.
 */
enum class IssuingCountry(val rawValue: String) {

    UNITED_STATES("USA"),
    CANADA("CAN");

    companion object {
        fun of(rawValue: String): IssuingCountry? =
            entries.firstOrNull { it.rawValue == rawValue }
    }
}
