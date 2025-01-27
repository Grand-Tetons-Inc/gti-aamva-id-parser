package net.prxnxt.aamvaparser.parser

import net.prxnxt.aamvaparser.models.Elements

/**
 * Published 03-2005.
 */
internal class VersionThreeParser(data: String) : AAMVAParser(data) {

    init {
        fields.remove(Elements.FIRST_NAME)
        fields.remove(Elements.MIDDLE_NAME)
        fields.remove(Elements.LAST_NAME_TRUNCATION)
        fields.remove(Elements.FIRST_NAME_TRUNCATION)
        fields.remove(Elements.MIDDLE_NAME_TRUNCATION)
        fields.remove(Elements.COMPLIANCE_TYPE)
        fields.remove(Elements.REVISION_DATE)
        fields.remove(Elements.HAZMAT_EXPIRATION_DATE)
        fields.remove(Elements.WEIGHT_POUNDS)
        fields.remove(Elements.WEIGHT_KILOGRAMS)
        fields.remove(Elements.IS_TEMPORARY_DOCUMENT)
        fields.remove(Elements.IS_ORGAN_DONOR)
        fields.remove(Elements.IS_VETERAN)
        fields.remove(Elements.DRIVER_LICENSE_NAME)
    }
}
