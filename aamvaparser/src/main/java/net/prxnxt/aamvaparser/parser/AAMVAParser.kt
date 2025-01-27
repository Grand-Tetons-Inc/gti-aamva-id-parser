package net.prxnxt.aamvaparser.parser

import net.prxnxt.aamvaparser.Utils
import net.prxnxt.aamvaparser.definitions.*
import net.prxnxt.aamvaparser.models.Elements
import net.prxnxt.aamvaparser.models.IdCard
import java.text.SimpleDateFormat
import java.util.*

/**
 * The primary class for parsing driver license data. This class automatically parses based on the
 * version number
 */
open class AAMVAParser(val data: String) {
    /**
     * The base fields common to all or most version standards. This
     * field should be modified in subclasses for version-specific
     * field changes.
     * */
    internal val fields: MutableMap<Elements, String> = mutableMapOf(
        Elements.JURISDICTION_VEHICLE_CLASS to "DCA",
        Elements.JURISDICTION_RESTRICTION_CODE to "DCB",
        Elements.JURISDICTION_ENDORSEMENT_CODE to "DCD",
        Elements.EXPIRATION_DATE to "DBA",
        Elements.ISSUE_DATE to "DBD",
        Elements.FIRST_NAME to "DAC",
        Elements.MIDDLE_NAME to "DAD",
        Elements.LAST_NAME to "DCS",
        Elements.BIRTH_DATE to "DBB",
        Elements.GENDER to "DBC",
        Elements.EYE_COLOR to "DAY",
        Elements.HEIGHT_INCHES to "DAU",
        Elements.STREET_ADDRESS to "DAG",
        Elements.CITY to "DAI",
        Elements.STATE to "DAJ",
        Elements.POSTAL_CODE to "DAK",
        Elements.DRIVER_LICENSE_NUMBER to "DAQ",
        Elements.UNIQUE_DOCUMENT_ID to "DCF",
        Elements.COUNTRY to "DCG",
        Elements.LAST_NAME_TRUNCATION to "DDE",
        Elements.FIRST_NAME_TRUNCATION to "DDF",
        Elements.MIDDLE_NAME_TRUNCATION to "DDG",
        Elements.STREET_ADDRESS_TWO to "DAH",
        Elements.HAIR_COLOR to "DAZ",
        Elements.PLACE_OF_BIRTH to "DCI",
        Elements.AUDIT_INFORMATION to "DCJ",
        Elements.INVENTORY_CONTROL_NUMBER to "DCK",
        Elements.LAST_NAME_ALIAS to "DBN",
        Elements.GIVEN_NAME_ALIAS to "DBG",
        Elements.SUFFIX to "DBS", //.name toDO toOr DCU
        Elements.WEIGHT_RANGE to "DCE",
        Elements.RACE to "DCL",
        Elements.STANDARD_VEHICLE_CODE to "DCM",
        Elements.STANDARD_ENDORSEMENT_CODE to "DCN",
        Elements.STANDARD_RESTRICTION_CODE to "DCO",
        Elements.JURISDICTION_VEHICLE_CLASS_DESCRIPTION to "DCP",
        Elements.JURISDICTION_ENDORSEMENT_CODE_DESCRIPTION to "DCQ",
        Elements.JURISDICTION_RESTRICTION_CODE_DESCRIPTION to "DCR",
        Elements.COMPLIANCE_TYPE to "DDA",
        Elements.REVISION_DATE to "DDB",
        Elements.HAZMAT_EXPIRATION_DATE to "DDC",
        Elements.WEIGHT_POUNDS to "DAW",
        Elements.WEIGHT_KILOGRAMS to "DAX",
        Elements.IS_TEMPORARY_DOCUMENT to "DDD",
        Elements.IS_ORGAN_DONOR to "DDK",
        Elements.IS_VETERAN to "DDL",
        Elements.FEDERAL_VEHICLE_CODE to "DCH",
        Elements.DRIVER_LICENSE_NAME to "DAA",
        Elements.GIVEN_NAME to "DCT"
    )

    /**
     * The version number detected in the driver license data or nil
     * if the data is not AAMVA compliant.
     */
//    val versionNumber get() = Utils.firstRegexMatch("\\d{6}(\\d{2})\\w+", data)?.toInt()
    val versionNumber: Int?
        get() {
            val matches = Regex("\\D(\\d{6}(\\d{2}))").find(data)
            val value = matches?.value
            return value?.substring(7)?.toIntOrNull()
        }

    /**
    The number of subfiles found in the driver license data.
     */
    private val subfileCount get() = Regex("\\d{8}(\\d{2})\\w+").find(data)?.value?.toInt()

    protected open val unitedStatesDateFormat get() = "MMddyyyy"
    protected open val canadaDateFormat get() = "yyyyMMdd"

    private val dateFormat
        get() = when (parsedCountry) {
            IssuingCountry.UNITED_STATES -> unitedStatesDateFormat
            IssuingCountry.CANADA -> canadaDateFormat
            else -> unitedStatesDateFormat
        }

    private val versionParser
        get() = when (versionNumber) {
            1 -> VersionOneParser(data)
            2 -> VersionTwoParser(data)
            3 -> VersionThreeParser(data)
            4 -> VersionFourParser(data)
            5 -> VersionFiveParser(data)
            6 -> VersionSixParser(data)
            7 -> VersionSevenParser(data)
            8 -> VersionEightParser(data)
            9 -> VersionNineParser(data)
            else -> AAMVAParser(data)
        }

    fun parse(): IdCard {
        val version = versionNumber
        val parser = versionParser
        return IdCard (
            parser.parsedFirstName,
            parser.parsedMiddleNames,
            parser.parsedLastName,
            parser.parseString(Elements.FIRST_NAME_ALIAS),
            parser.parseString(Elements.GIVEN_NAME_ALIAS),
            parser.parseString(Elements.LAST_NAME_ALIAS),
            parser.parseString(Elements.SUFFIX_ALIAS),
            parser.parsedNameSuffix,
            parser.parseTruncation(Elements.FIRST_NAME_TRUNCATION),
            parser.parseTruncation(Elements.MIDDLE_NAME_TRUNCATION),
            parser.parseTruncation(Elements.LAST_NAME_TRUNCATION),
            parser.parseDate(Elements.EXPIRATION_DATE),
            parser.parseDate(Elements.ISSUE_DATE),
            parser.parseDate(Elements.BIRTH_DATE),
            parser.parseDate(Elements.HAZMAT_EXPIRATION_DATE),
            parser.parseDate(Elements.REVISION_DATE),
            parser.parseString(Elements.RACE),
            parser.parsedGender,
            parser.parsedEyeColor,
            parser.parsedHeight,
            parser.parsedWeight,
            parser.parsedHairColor,
            parser.parseString(Elements.PLACE_OF_BIRTH),
            parser.parseString(Elements.STREET_ADDRESS),
            parser.parseString(Elements.STREET_ADDRESS_TWO),
            parser.parseString(Elements.CITY),
            parser.parseString(Elements.STATE),
            parser.parsedPostalCode,
            parser.parsedCountry,
            parser.parseString(Elements.DRIVER_LICENSE_NUMBER),
            parser.parseString(Elements.UNIQUE_DOCUMENT_ID),
            parser.parseString(Elements.AUDIT_INFORMATION),
            parser.parseString(Elements.INVENTORY_CONTROL_NUMBER),
            parser.parseString(Elements.COMPLIANCE_TYPE),
            parser.parseBoolean(Elements.IS_ORGAN_DONOR),
            parser.parseBoolean(Elements.IS_VETERAN),
            parser.parseBoolean(Elements.IS_TEMPORARY_DOCUMENT),
            parser.parseString(Elements.FEDERAL_VEHICLE_CODE),
            parser.parseString(Elements.STANDARD_VEHICLE_CODE),
            parser.parseString(Elements.STANDARD_RESTRICTION_CODE),
            parser.parseString(Elements.STANDARD_ENDORSEMENT_CODE),
            parser.parseString(Elements.JURISDICTION_VEHICLE_CLASS),
            parser.parseString(Elements.JURISDICTION_RESTRICTION_CODE),
            parser.parseString(Elements.JURISDICTION_ENDORSEMENT_CODE),
            parser.parseString(Elements.JURISDICTION_VEHICLE_CLASS_DESCRIPTION),
            parser.parseString(Elements.JURISDICTION_RESTRICTION_CODE_DESCRIPTION),
            parser.parseString(Elements.JURISDICTION_ENDORSEMENT_CODE_DESCRIPTION),
            version,
            data
        )
    }

    internal open fun parseString(key: Elements): String? {
        fields[key]?.let {
            return Regex("$it(.+)\\b").find(data)?.value?.removePrefix(it)
        } ?: return null
    }

    internal open fun parseDouble(key: Elements): Double? {
        fields[key]?.let {
            return Regex("$it(\\w+)\\b").find(data)?.value?.removePrefix(it)?.toDoubleOrNull()
        } ?: return null
    }

    internal open fun parseDate(key: Elements): Date? {
        val dateString = parseString(key)
        if (dateString.isNullOrEmpty()) return null
        return SimpleDateFormat(dateFormat, Locale.US).parse(dateString)
    }

    internal open fun parseBoolean(key: Elements): Boolean? {
        val rawValue = parseString(key) ?: return null
        return rawValue == "1"
    }

    protected open val parsedFirstName
        get() =
            parseString(Elements.FIRST_NAME)
                    ?: parseString(Elements.GIVEN_NAME)?.split(",")?.lastOrNull()?.trim()
                    ?: parseString(Elements.DRIVER_LICENSE_NAME)?.split(",")?.lastOrNull()?.trim()

    protected open val parsedMiddleNames: List<String>
        get() {
            parseString(Elements.MIDDLE_NAME)?.let {
                return listOf(it)
            }

            parseString(Elements.GIVEN_NAME)?.let {
                val parts = it.split(",")
                return parts.drop(0).map { it.trim() }
            }

            parseString(Elements.DRIVER_LICENSE_NAME)?.let {
                val parts = it.split(",")
                return parts.drop(0).dropLast(0).map { it.trim() }
            }
            return listOf()
        }

    protected open val parsedLastName
        get() = parseString(Elements.LAST_NAME)
                ?: parseString(Elements.DRIVER_LICENSE_NAME)?.split(",")?.lastOrNull()?.trim()

    protected open val parsedNameSuffix: NameSuffix?
        get() {
            return NameSuffix.of(parseString(Elements.SUFFIX) ?: return null)
        }

    internal open fun parseTruncation(key: Elements): Truncation? {
        parseString(key)?.let {
            return Truncation.of(it)
        } ?: return null
    }

    protected open val parsedCountry: IssuingCountry?
        get() {
            return IssuingCountry.of(parseString(Elements.COUNTRY) ?: return null)
        }

    protected open val parsedGender: Gender?
        get() {
            return Gender.of(parseString(Elements.GENDER) ?: return null)
        }

    protected open val parsedEyeColor: EyeColor?
        get() {
            return EyeColor.of(parseString(Elements.EYE_COLOR) ?: return null)
        }

    protected open val parsedHairColor: HairColor?
        get() {
            return HairColor.of(parseString(Elements.HAIR_COLOR) ?: return null)
        }

    /**
     * Returns the height in inches.
     */
    protected open val parsedHeight: Double?
        get() {
            val heightString = parseString(Elements.HEIGHT_INCHES) ?: return null
            val height = heightString.split(" ")
                .firstOrNull()?.toDoubleOrNull() ?: return null
            return if (heightString.contains("cm"))
                Utils.inchesFromCentimeters(height) else height
        }

    /**
     * Returns the weight in pounds.
     */
    protected open val parsedWeight: Weight?
        get() {
            parseString(Elements.WEIGHT_POUNDS)?.toDoubleOrNull()?.let {
                return Weight(pounds = it)
            }
            parseString(Elements.WEIGHT_KILOGRAMS)?.toDoubleOrNull()?.let {
                return Weight(pounds = Utils.poundsFromKilograms(it))
            }
            parseString(Elements.WEIGHT_RANGE)?.toIntOrNull()?.let {
                return Weight(WeightRange(it))
            }
            return null
        }

    /**
     * Returns the postal code in 12345-6789 format or 12345 if the last 4 digits are 0.
     */
    protected open val parsedPostalCode: String?
        get() {
            val rawCode = parseString(Elements.POSTAL_CODE)
            val firstPart: String? = rawCode?.substring(0, 5) ?: return null
            val secondPart: String? = rawCode.substring(5)

            secondPart?.takeIf { it != "0000" }?.let { return firstPart.plus("-").plus(it) }
                    ?: return firstPart
        }
}
