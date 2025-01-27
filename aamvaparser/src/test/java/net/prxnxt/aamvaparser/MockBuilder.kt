package tests

import net.prxnxt.aamvaparser.definitions.ReservedKeywords
import net.prxnxt.aamvaparser.parser.AAMVAParser


class MockBuilder(val versionParser: AAMVAParser) {

    val issuerIdentificationNumber = "636000"
    val numberOfSubfiles = "01"
    val jurisdictionVersionNumber = "01"

    fun makeHeader(): String {
        val lineOne = ReservedKeywords.COMPLIANCE_INDICATOR.rawValue +
                ReservedKeywords.DATA_ELEMENT_SEPARATOR +
                ReservedKeywords.RECORD_SEPARATOR +
                ReservedKeywords.SEGMENT_SEPARATOR
        val version = versionParser.versionNumber ?: 0
        val lineTwo = ReservedKeywords.FILE_TYPE.rawValue +
                issuerIdentificationNumber +
                String.format("%2d", version) +
                jurisdictionVersionNumber +
                numberOfSubfiles
        return lineOne.plus(lineTwo)
    }
}
