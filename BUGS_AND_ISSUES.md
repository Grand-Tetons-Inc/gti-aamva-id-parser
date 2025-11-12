# Bugs and Issues

This document tracks all identified bugs, issues, and technical debt in the GTI AAMVA ID Parser project.

## Critical Issues

### 🔴 CRITICAL-001: Missing Build System
**Status:** Open
**Priority:** P0 (Blocker)
**Severity:** Critical

**Description:**
The project cannot be built as-is. Essential Gradle configuration files are missing.

**Missing Files:**
- `settings.gradle.kts` (or `.gradle`)
- Root `build.gradle.kts` (or `.gradle`)
- `gradlew` and `gradlew.bat`
- `gradle/wrapper/gradle-wrapper.jar`
- `gradle/wrapper/gradle-wrapper.properties`
- `gradle.properties`

**Impact:**
- Cannot build the library
- Cannot run tests
- Cannot publish to Maven
- Cannot use in other projects via dependency

**Suggested Fix:**
Create minimum viable Gradle configuration:

```kotlin
// settings.gradle.kts
rootProject.name = "gti-aamva-id-parser"
include(":aamvaparser")
```

```kotlin
// build.gradle.kts (root)
plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
}
```

**Estimated Effort:** 2-4 hours

---

### 🔴 CRITICAL-002: StringIndexOutOfBoundsException in Postal Code Parsing
**Status:** Open
**Priority:** P0 (Critical Bug)
**Severity:** High
**File:** `AAMVAParser.kt`
**Lines:** 286-287

**Description:**
The `parsedPostalCode` property will throw `StringIndexOutOfBoundsException` if the postal code is less than 5 characters.

**Current Code:**
```kotlin
protected open val parsedPostalCode: String?
    get() {
        val rawCode = parseString(Elements.POSTAL_CODE)
        val firstPart: String? = rawCode?.substring(0, 5) ?: return null
        val secondPart: String? = rawCode.substring(5)
        // ...
    }
```

**Problem:**
- If `rawCode` is "123" (3 chars), `substring(0, 5)` throws exception
- If `rawCode` is "12345" but malformed data continues, `substring(5)` could fail

**Suggested Fix:**
```kotlin
protected open val parsedPostalCode: String?
    get() {
        val rawCode = parseString(Elements.POSTAL_CODE) ?: return null

        // Return as-is if too short
        if (rawCode.length < 5) return rawCode

        val firstPart = rawCode.substring(0, 5)
        val secondPart = if (rawCode.length > 5) rawCode.substring(5) else "0000"

        return if (secondPart != "0000") {
            "$firstPart-$secondPart"
        } else {
            firstPart
        }
    }
```

**Estimated Effort:** 30 minutes

---

### 🔴 CRITICAL-003: No Test Coverage
**Status:** Open
**Priority:** P0 (Blocker)
**Severity:** Critical
**File:** `ExampleUnitTest.kt`

**Description:**
The project has zero meaningful test coverage. Only contains a placeholder test `addition_isCorrect()`.

**Impact:**
- No confidence in parser correctness
- Cannot detect regressions
- Unsafe for production use
- Cannot verify bug fixes

**Required Tests:**
1. Unit tests for each version parser (1-9)
2. Edge case tests (empty data, malformed data, null values)
3. Date parsing tests (US vs Canada formats)
4. Unit conversion tests
5. Regex pattern tests
6. Integration tests with real barcode samples

**Suggested Approach:**
```kotlin
class AAMVAParserTest {
    @Test
    fun `parse version 9 license with all fields`() {
        val sampleData = loadTestData("v9_complete.txt")
        val parser = AAMVAParser(sampleData)
        val card = parser.parse()

        assertEquals("JOHN", card.firstName)
        assertEquals("DOE", card.lastName)
        assertEquals("123456789", card.licenseNumber)
        assertEquals(9, card.version)
        // ... more assertions
    }

    @Test
    fun `handles missing optional fields gracefully`() { }

    @Test
    fun `detects expired licenses correctly`() { }

    @Test
    fun `throws exception for invalid data`() { }
}
```

**Estimated Effort:** 40-60 hours

---

## High Priority Issues

### 🟠 HIGH-001: Logic Error in Middle Name Parsing
**Status:** Open
**Priority:** P1
**Severity:** Medium
**File:** `AAMVAParser.kt`
**Lines:** 207, 211

**Description:**
The middle name parsing logic has `drop(0)` which doesn't actually drop any elements.

**Current Code:**
```kotlin
// Line 207
parseString(Elements.GIVEN_NAME)?.let {
    val parts = it.split(",")
    return parts.drop(0).map { it.trim() }  // drop(0) does nothing!
}

// Line 211
parseString(Elements.DRIVER_LICENSE_NAME)?.let {
    val parts = it.split(",")
    return parts.drop(0).dropLast(0).map { it.trim() }  // Neither does anything!
}
```

**Problem:**
- `drop(0)` returns all elements (drops none)
- `dropLast(0)` returns all elements (drops none)
- Logic doesn't match the apparent intent

**Expected Behavior:**
For "SMITH,JOHN,MICHAEL,ALEXANDER", should return ["MICHAEL", "ALEXANDER"] (all middle names).

**Suggested Fix:**
```kotlin
parseString(Elements.GIVEN_NAME)?.let {
    val parts = it.split(",")
    // Skip first element (last name), return rest as middle names
    return parts.drop(1).map { it.trim() }
}

parseString(Elements.DRIVER_LICENSE_NAME)?.let {
    val parts = it.split(",")
    // Skip first (last name) and last (first name), middle is what remains
    return if (parts.size > 2) {
        parts.subList(1, parts.size - 1).map { it.trim() }
    } else {
        emptyList()
    }
}
```

**Estimated Effort:** 1 hour (including tests)

---

### 🟠 HIGH-002: Incomplete TODO - Suffix Field Mapping
**Status:** Open
**Priority:** P1
**Severity:** Low
**File:** `AAMVAParser.kt`
**Line:** 50

**Current Code:**
```kotlin
Elements.SUFFIX to "DBS", //.name toDO toOr DCU
```

**Description:**
Comment indicates uncertainty whether suffix should map to "DBS" or "DCU". Needs verification against AAMVA specifications.

**Research Needed:**
- Check AAMVA v1-9 specs for suffix field codes
- Determine if different versions use different codes
- May need version-specific mapping

**Suggested Action:**
1. Review AAMVA standards documentation
2. Verify which versions use DBS vs DCU
3. Either remove TODO or add version-specific override
4. Document the decision

**Estimated Effort:** 2-3 hours (research + implementation)

---

## Medium Priority Issues

### 🟡 MEDIUM-001: Missing Country Field Documentation
**Status:** Open
**Priority:** P2
**Severity:** Low
**File:** `VersionOneParser.kt`
**Line:** 17

**Current Code:**
```kotlin
fields.remove(Elements.COUNTRY) // TODO: No documentation?
```

**Description:**
Developer unsure if COUNTRY field exists in AAMVA Version 1. Needs verification.

**Action Items:**
- Review AAMVA Version 1 (2000) specification
- Determine if COUNTRY field (DCG) exists
- Update code accordingly
- Document decision

**Estimated Effort:** 1-2 hours

---

### 🟡 MEDIUM-002: Unused Property - subfileCount
**Status:** Open
**Priority:** P2
**Severity:** Low
**File:** `AAMVAParser.kt`
**Line:** 87

**Current Code:**
```kotlin
private val subfileCount get() = Regex("\\d{8}(\\d{2})\\w+").find(data)?.value?.toInt()
```

**Description:**
Property is defined but never used anywhere in the codebase.

**Options:**
1. Remove if truly unnecessary
2. Use for validation (verify expected vs actual subfiles)
3. Include in IdCard model for debugging

**Suggested Fix:**
Either remove or use for validation:
```kotlin
private fun validateSubfileCount() {
    val expected = subfileCount
    val actual = // count actual subfiles in data
    if (expected != actual) {
        throw ParseException("Expected $expected subfiles, found $actual")
    }
}
```

**Estimated Effort:** 30 minutes

---

### 🟡 MEDIUM-003: Commented Out Code
**Status:** Open
**Priority:** P2
**Severity:** Low
**File:** `AAMVAParser.kt`
**Line:** 76

**Current Code:**
```kotlin
//    val versionNumber get() = Utils.firstRegexMatch("\\d{6}(\\d{2})\\w+", data)?.toInt()
```

**Description:**
Old implementation left commented out. Should be removed.

**Action:**
Remove commented code. If needed for reference, it's in git history.

**Estimated Effort:** 5 minutes

---

## Low Priority Issues

### 🟢 LOW-001: Regex Compilation Performance
**Status:** Open
**Priority:** P3
**Severity:** Low
**Files:** Multiple

**Description:**
Regex patterns are compiled on every method call, which is inefficient.

**Current Pattern:**
```kotlin
internal open fun parseString(key: Elements): String? {
    fields[key]?.let {
        return Regex("$it(.+)\\b").find(data)?.value?.removePrefix(it)
    } ?: return null
}
```

**Problem:**
- Creates new Regex object for every field parsed
- Same pattern may be compiled multiple times
- Impacts performance for large batches

**Suggested Fix:**
```kotlin
companion object {
    private val regexCache = mutableMapOf<String, Regex>()

    private fun getCompiledRegex(pattern: String): Regex {
        return regexCache.getOrPut(pattern) { Regex(pattern) }
    }
}

internal open fun parseString(key: Elements): String? {
    fields[key]?.let {
        val pattern = "$it(.+)\\b"
        return getCompiledRegex(pattern).find(data)?.value?.removePrefix(it)
    } ?: return null
}
```

**Estimated Effort:** 2-3 hours

---

### 🟢 LOW-002: Magic String Literals
**Status:** Open
**Priority:** P3
**Severity:** Low
**Files:** Multiple

**Description:**
Field codes ("DCS", "DAC", etc.) are string literals without documentation.

**Current:**
```kotlin
Elements.LAST_NAME to "DCS",
Elements.FIRST_NAME to "DAC",
```

**Suggested Improvement:**
```kotlin
// Create constants with documentation
object FieldCodes {
    /** Customer Last Name */
    const val LAST_NAME = "DCS"

    /** Customer First Name */
    const val FIRST_NAME = "DAC"

    // ... etc
}

// Use in mapping
Elements.LAST_NAME to FieldCodes.LAST_NAME,
Elements.FIRST_NAME to FieldCodes.FIRST_NAME,
```

**Estimated Effort:** 3-4 hours

---

### 🟢 LOW-003: Missing Package Documentation
**Status:** Open
**Priority:** P3
**Severity:** Low

**Description:**
Most classes lack KDoc documentation comments.

**Action Items:**
- Add class-level KDoc to all public classes
- Document all public methods
- Add usage examples in KDoc
- Document parameters and return values
- Add `@since` tags for version tracking

**Estimated Effort:** 8-10 hours

---

### 🟢 LOW-004: No Input Validation
**Status:** Open
**Priority:** P3
**Severity:** Low

**Description:**
Parser doesn't validate input before attempting to parse.

**Suggested Additions:**
```kotlin
fun parse(): IdCard {
    require(data.isNotEmpty()) { "Barcode data cannot be empty" }
    require(data.startsWith("@")) {
        "Invalid AAMVA format: missing compliance indicator '@'"
    }
    require(versionNumber != null) {
        "Could not detect AAMVA version number"
    }
    require(versionNumber in 1..9) {
        "Unsupported AAMVA version: $versionNumber"
    }

    // ... proceed with parsing
}
```

**Estimated Effort:** 2-3 hours

---

## Documentation Issues

### 📄 DOC-001: README Lacks Usage Examples
**Status:** Open
**Priority:** P2

**Current README:**
```markdown
# gti-aamva-id-parser
Parses Identification Cards that adhere to the AAMVA standards
```

**Needs:**
- Installation instructions
- Basic usage example
- Feature list
- Supported versions
- Requirements
- License information
- Contributing guidelines
- Link to documentation

**Estimated Effort:** 3-4 hours

---

### 📄 DOC-002: No API Documentation
**Status:** Open
**Priority:** P2

**Needs:**
- Generated KDoc HTML
- Field mapping reference
- Version differences document
- Error handling guide

**Estimated Effort:** 8-10 hours

---

## Infrastructure Issues

### ⚙️ INFRA-001: No CI/CD Pipeline
**Status:** Open
**Priority:** P2

**Needs:**
- GitHub Actions workflow
- Automated testing on PR
- Code coverage reporting
- Lint checks
- Automated releases

**Estimated Effort:** 6-8 hours

---

### ⚙️ INFRA-002: No Dependency Management
**Status:** Open
**Priority:** P2

**Description:**
`build.gradle.kts` references `libs.plugins.*` but no version catalog exists.

**Missing:**
- `gradle/libs.versions.toml`

**Estimated Effort:** 1-2 hours

---

### ⚙️ INFRA-003: No Publishing Configuration
**Status:** Open
**Priority:** P2

**Needs:**
- Maven publishing setup
- POM configuration
- Artifact signing
- Release documentation

**Estimated Effort:** 4-6 hours

---

## Summary

| Severity | Open | In Progress | Closed |
|----------|------|-------------|--------|
| Critical | 3    | 0           | 0      |
| High     | 2    | 0           | 0      |
| Medium   | 3    | 0           | 0      |
| Low      | 4    | 0           | 0      |
| Docs     | 2    | 0           | 0      |
| Infra    | 3    | 0           | 0      |
| **Total**| **17**| **0**      | **0**  |

---

## How to Contribute

If you'd like to fix any of these issues:

1. Fork the repository
2. Create a branch: `git checkout -b fix/ISSUE-NUMBER`
3. Make your changes
4. Add tests
5. Submit a pull request referencing the issue number

---

**Last Updated:** November 12, 2025
