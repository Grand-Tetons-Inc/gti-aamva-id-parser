package net.prxnxt.aamvaparser.definitions

/**
 * The AAMVA hair color types.
 */
enum class HairColor(val rawValue: String) {

    BALD("BAL"),
    BLACK("BLK"),
    BLOND("BLN"),
    BROWN("BRO"),
    GREY("GRY"),
    RED("RED"),
    SANDY("SDY"),
    WHITE("WHI");

    companion object {
        fun of(rawValue: String): HairColor? =
            entries.firstOrNull { it.rawValue == rawValue }
    }
}
