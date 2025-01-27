package net.prxnxt.aamvaparser.definitions

/**
 * The AAMVA eye color types.
 */
enum class EyeColor(val rawValue: String) {

    BLACK("BLK"),
    BLUE("BLU"),
    BROWN("BRO"),
    GRAY("GRY"),
    GREEN("GRN"),
    HAZEL("HAZ"),
    MAROON("MAR"),
    PINK("PNK"),
    DICHROMATIC("DIC");

    companion object {
        fun of(rawValue: String): EyeColor? =
            entries.firstOrNull { it.rawValue == rawValue }
    }
}
