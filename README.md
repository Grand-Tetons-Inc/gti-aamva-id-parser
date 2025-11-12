# GTI AAMVA ID Parser

![Status](https://img.shields.io/badge/status-pre--alpha-red)
![License](https://img.shields.io/badge/license-MIT-blue)
![Platform](https://img.shields.io/badge/platform-Android-green)
![Language](https://img.shields.io/badge/language-Kotlin-purple)

A Kotlin library for parsing driver's licenses and identification cards that conform to the AAMVA (American Association of Motor Vehicle Administrators) PDF417 barcode standard.

> ⚠️ **Pre-Alpha Notice:** This library is currently in early development and not yet production-ready. See [Project Status](#project-status) for details.

---

## Features

- ✅ **Multi-Version Support:** Parses AAMVA standards versions 1-9 (2000-2016)
- ✅ **Automatic Version Detection:** Automatically identifies and routes to the correct parser
- ✅ **Comprehensive Data Extraction:** Extracts 60+ fields including personal info, physical characteristics, addresses, and license details
- ✅ **Type-Safe:** Strongly-typed Kotlin enums for standardized values (gender, eye color, etc.)
- ✅ **Null-Safe:** Proper handling of optional and missing fields
- ✅ **Multi-Country:** Supports both US and Canadian formats
- ✅ **Convenience Methods:** Built-in validation (expired, juvenile, acceptable)

---

## What is AAMVA?

The [AAMVA](https://www.aamva.org/) defines standards for encoding driver's license data in PDF417 2D barcodes found on the back of most US and Canadian driver's licenses. This library decodes that barcode data into a structured format.

---

## Installation

> ⚠️ **Not Yet Available:** The library is not yet published. See [Project Status](#project-status).

Once published, add to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("net.prxnxt:aamvaparser:1.0.0")
}
```

---

## Usage

### Basic Example

```kotlin
import net.prxnxt.aamvaparser.parser.AAMVAParser

// Get barcode data from your scanner (ZXing, ML Kit, etc.)
val barcodeData: String = scanBarcode()

// Parse the data
val parser = AAMVAParser(barcodeData)
val idCard = parser.parse()

// Access parsed information
println("Name: ${idCard.firstName} ${idCard.lastName}")
println("License #: ${idCard.licenseNumber}")
println("DOB: ${idCard.birthdate}")
println("Expires: ${idCard.expirationDate}")
println("AAMVA Version: ${idCard.version}")
```

### Validation

```kotlin
// Check if license is valid
when {
    !idCard.isAcceptable -> println("Insufficient data")
    idCard.isExpired -> println("License expired!")
    idCard.isJuvenile -> println("Holder is under 18")
    else -> println("License is valid")
}
```

---

## Supported AAMVA Versions

| Version | Published | Status |
|---------|-----------|--------|
| 1       | 2000      | ✅ Supported |
| 2       | 09-2003   | ✅ Supported |
| 3       | 03-2005   | ✅ Supported |
| 4       | 07-2009   | ✅ Supported |
| 5       | 07-2010   | ✅ Supported |
| 6       | 07-2011   | ✅ Supported |
| 7       | 06-2012   | ✅ Supported |
| 8       | 08-2013   | ✅ Supported |
| 9       | 2016      | ✅ Supported |
| 10+     | Future    | 🔮 Planned |

---

## Project Status

### Current State: Pre-Alpha ⚠️

This library is in early development and **not production-ready** due to:

- ❌ Missing build configuration (cannot build as-is)
- ❌ No test coverage
- ❌ Known critical bugs
- ❌ Incomplete documentation

### Documentation

- 📖 **[PROJECT_ANALYSIS.md](PROJECT_ANALYSIS.md)** - Comprehensive project analysis
- 🐛 **[BUGS_AND_ISSUES.md](BUGS_AND_ISSUES.md)** - Known issues and bug tracking
- 🗺️ **[ROADMAP.md](ROADMAP.md)** - Development roadmap and timeline

**Estimated v1.0.0 Release:** 4-5 weeks

---

## Requirements

- **Android SDK:** minSdk 24 (Android 7.0), compileSdk 35
- **Kotlin:** 1.9+
- **Java:** 11

---

## Contributing

We welcome contributions! Check [BUGS_AND_ISSUES.md](BUGS_AND_ISSUES.md) for tasks.

---

## License

MIT License - See [LICENSE](LICENSE) for details.

---

**Maintained By:** Grand-Tetons-Inc
**Last Updated:** November 12, 2025
