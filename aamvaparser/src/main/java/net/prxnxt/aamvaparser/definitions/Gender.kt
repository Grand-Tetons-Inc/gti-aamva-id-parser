package net.prxnxt.aamvaparser.definitions

/**
 * The AAMVA gender types.
 */
enum class Gender(val rawValue: String) {

    MALE("1"),
    FEMALE("2");

    companion object {
        fun of(rawValue: String): Gender? =
            entries.firstOrNull { it.rawValue == rawValue }
    }
}
