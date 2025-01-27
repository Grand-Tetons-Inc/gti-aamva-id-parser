package net.prxnxt.aamvaparser.parser

import net.prxnxt.aamvaparser.models.Elements

/**
 * Published 07-2010.
 */
internal class VersionFiveParser(data: String) : AAMVAParser(data) {

    init {
        fields.remove(Elements.IS_ORGAN_DONOR)
        fields.remove(Elements.IS_VETERAN)
        fields.remove(Elements.FEDERAL_VEHICLE_CODE)
        fields.remove(Elements.DRIVER_LICENSE_NAME)
        fields.remove(Elements.GIVEN_NAME)
    }
}
