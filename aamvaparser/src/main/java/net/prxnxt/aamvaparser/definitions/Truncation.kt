package net.prxnxt.aamvaparser.definitions

/**
 * The AAMVA truncation types.
 */
enum class Truncation(val rawValue: String) {

    TRUNCATED("T"),
    NONE("N");

    companion object {
        fun of(rawValue: String): Truncation? =
            entries.firstOrNull { it.rawValue == rawValue }
    }
}
