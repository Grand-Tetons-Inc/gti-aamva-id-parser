package net.prxnxt.aamvaparser.parser

import net.prxnxt.aamvaparser.models.Elements

/**
 * Published 08-2013.
 */
internal class VersionEightParser(data: String) : AAMVAParser(data) {
    
    init {
        fields.remove(Elements.FEDERAL_VEHICLE_CODE)
        fields.remove(Elements.DRIVER_LICENSE_NAME)
        fields.remove(Elements.GIVEN_NAME)
    }
}
