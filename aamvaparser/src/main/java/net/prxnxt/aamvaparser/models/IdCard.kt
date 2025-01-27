package net.prxnxt.aamvaparser.models

import net.prxnxt.aamvaparser.definitions.*
import java.util.*

data class IdCard(
    var firstName: String? = null,
    var middleNames: List<String> = listOf(),
    var lastName: String? = null,

    var firstNameAlias: String? = null,
    var givenNameAlias: String? = null,
    var lastNameAlias: String? = null,
    var suffixAlias: String? = null,
    var suffix: NameSuffix? = null,
    var firstNameTruncation: Truncation? = null,
    var middleNameTruncation: Truncation? = null,
    var lastNameTruncation: Truncation? = null,

    var expirationDate: Date? = null,
    var issuedDate: Date? = null,
    var birthdate: Date? = null,
    var hazmatExpirationDate: Date? = null,
    var revisionDate: Date? = null,

    var race: String? = null,
    var gender: Gender? = null,
    var eyeColor: EyeColor? = null,
    var height: Double? = null,
    var weight: Weight? = null,
    var hairColor: HairColor?,

    var placeOfBirth: String? = null,
    var streetAddress: String? = null,
    var streetAddressTwo: String? = null,
    var city: String? = null,
    var state: String? = null,
    var postalCode: String? = null,
    var country: IssuingCountry?,

    var licenseNumber: String? = null,
    var documentId: String? = null,
    var auditInformation: String? = null,
    var inventoryControlNumber: String? = null,
    var complianceType: String? = null,
    var isOrganDonor: Boolean? = null,
    var isVeteran: Boolean? = null,
    var isTemporaryDocument: Boolean? = null,

    var federalVehicleCode: String? = null,
    var standardVehicleCode: String? = null,
    var standardRestrictionCode: String? = null,
    var standardEndorsementCode: String? = null,

    var jurisdictionVehicleCode: String? = null,
    var jurisdictionRestrictionCode: String? = null,
    var jurisdictionEndorsementCode: String? = null,

    var jurisdictionVehicleDescription: String? = null,
    var jurisdictionRestrictionDescription: String? = null,
    var jurisdictionEndorsementDescription: String? = null,

    var version: Int? = null,
    var pdf417Data: String? = null
) {
    /**
     * Determines if the license is expired based on the `expirationDate`
     */
    val isExpired: Boolean
        get() {
            return Date() > (expirationDate ?: return false)
        }

    /**
     * Determines if the license has been issued based on the `issueDate`
     */
    val isIssued: Boolean
        get() {
            return Date() > (issuedDate ?: return false)
        }

    /**
     * Determines if enough of the license data is present to be useful for most things.
     */
    val isAcceptable get() = licenseNumber != null

    /**
     * Determines if the licensed driver is under 18.
     */
    val isJuvenile: Boolean
        get() {
            val birthdate = this.birthdate ?: return false

            val today = Calendar.getInstance()
            val birthdateCalendar = Calendar.getInstance().apply { time = birthdate }

            var years = today.get(Calendar.YEAR) - birthdateCalendar.get(Calendar.YEAR)

            if (today.get(Calendar.DAY_OF_YEAR) < birthdateCalendar.get(Calendar.DAY_OF_YEAR)) {
                years--
            }
            return years < 18
        }
}