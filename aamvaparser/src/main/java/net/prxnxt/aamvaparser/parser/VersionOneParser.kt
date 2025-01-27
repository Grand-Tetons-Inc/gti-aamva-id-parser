package net.prxnxt.aamvaparser.parser

import net.prxnxt.aamvaparser.Utils
import net.prxnxt.aamvaparser.models.Elements

/**
 * Published 2000.
 */
internal class VersionOneParser(data: String) : AAMVAParser(data) {

    init {
        fields.remove(Elements.JURISDICTION_VEHICLE_CLASS)
        fields.remove(Elements.JURISDICTION_RESTRICTION_CODE)
        fields.remove(Elements.JURISDICTION_ENDORSEMENT_CODE)
        fields[Elements.LAST_NAME] = "DAB"
        fields[Elements.UNIQUE_DOCUMENT_ID] = "DBJ"
        fields.remove(Elements.COUNTRY) // TODO: No documentation?
        fields.remove(Elements.LAST_NAME_TRUNCATION)
        fields.remove(Elements.FIRST_NAME_TRUNCATION)
        fields.remove(Elements.MIDDLE_NAME_TRUNCATION)
        fields.remove(Elements.PLACE_OF_BIRTH)
        fields.remove(Elements.AUDIT_INFORMATION)
        fields.remove(Elements.INVENTORY_CONTROL_NUMBER)
        fields[Elements.LAST_NAME_ALIAS] = "DBO"
        fields[Elements.GIVEN_NAME_ALIAS] = "DBP"
        fields[Elements.SUFFIX_ALIAS] = "DBR"
        fields[Elements.SUFFIX] = "DAE"
        fields[Elements.HEIGHT_CENTIMETERS] = "DAV"
        fields.remove(Elements.WEIGHT_RANGE)
        fields.remove(Elements.RACE)
        fields[Elements.STANDARD_VEHICLE_CODE] = "PAA"
        fields[Elements.STANDARD_ENDORSEMENT_CODE] = "PAF"
        fields[Elements.STANDARD_RESTRICTION_CODE] = "PAE"
        fields.remove(Elements.JURISDICTION_VEHICLE_CLASS_DESCRIPTION)
        fields.remove(Elements.JURISDICTION_ENDORSEMENT_CODE_DESCRIPTION)
        fields.remove(Elements.JURISDICTION_RESTRICTION_CODE_DESCRIPTION)
        fields.remove(Elements.COMPLIANCE_TYPE)
        fields.remove(Elements.REVISION_DATE)
        fields.remove(Elements.HAZMAT_EXPIRATION_DATE)
        fields.remove(Elements.IS_TEMPORARY_DOCUMENT)
        fields[Elements.IS_ORGAN_DONOR] = "DBH"
        fields.remove(Elements.IS_VETERAN)
    }

    override val unitedStatesDateFormat = "yyyyMMdd"

    override val parsedHeight: Double?
        get() {
            // Check for cm
            parseString(Elements.HEIGHT_CENTIMETERS)?.toDoubleOrNull()?.let {
                return Utils.inchesFromCentimeters(it)
            }

            // Check for ft/in
            val rawHeight = parseString(Elements.HEIGHT_INCHES)?.toIntOrNull() ?: return null
            val feet = rawHeight / 100
            val inches = rawHeight - (feet * 100)
            val totalInches = (feet * 12) + inches
            return totalInches.toDouble()
        }
}
