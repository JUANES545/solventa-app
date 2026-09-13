# Solventa Mobile App

[![Android CI](https://github.com/JUANES545/solventa-app/actions/workflows/android-ci.yml/badge.svg?branch=develop)](https://github.com/JUANES545/solventa-app/actions/workflows/android-ci.yml)
[![Latest release](https://img.shields.io/github/v/release/JUANES545/solventa-app)](https://github.com/JUANES545/solventa-app/releases/latest)

[Download the latest signed APK](https://github.com/JUANES545/solventa-app/releases/latest/download/solventa.apk)

Solventa is a mobile insurance application designed to help customers quote, purchase, and manage insurance policies from one place.

The application will provide access to policy information, claims reporting and tracking, payment reminders, notifications, and customer profile settings. It will also represent consent and identity-verification flows required for digital insurance services.

## Project scope

This repository contains the Android client prototype developed with Kotlin and Jetpack Compose. It uses simulated data while keeping repository boundaries ready for a future connection to Solventa's Mobile BFF.

## Technology

- Kotlin
- Jetpack Compose
- Material 3
- MVVM
- Hilt
- Coroutines and StateFlow
- Navigation Compose
- DataStore

## Prototype journey

Travel insurance provides the complete journey: quote, plan selection, consent, simulated payment, simulated OTP signature, policy issuance, policy detail, and claim reporting. Life, device, and parametric products are represented through sample catalog and policy data.

The claim flow uses Android Photo Picker for local image selection and the device location permission with manual and demo-location alternatives. Nothing is uploaded to a server.

## Run the app

Requirements: JDK 17, Android SDK 35, and a phone emulator or device running Android 8.0 (API 26) or newer.

1. Open this repository in Android Studio.
2. Allow Gradle synchronization to complete.
3. Select a phone emulator or connected device.
4. Run the `app` configuration.

Demo account:

- Email: `alvaro.mejia@solventa.co`
- Password: `Solventa123`
- OTP: `123456`

The **Enter as test user** button provides a quicker demo entry.

## Verification

```powershell
./gradlew.bat lintDebug
./gradlew.bat testDebugUnitTest
./gradlew.bat assembleDebugAndroidTest
./gradlew.bat assembleDebug
./gradlew.bat assembleRelease
```

Connected UI tests require an emulator or physical device:

```powershell
./gradlew.bat connectedDebugAndroidTest
```

## Releases

Every update merged into `main` is verified by GitHub Actions. If unit tests, lint, and the navigation smoke tests pass, the workflow builds a signed APK and publishes a GitHub Release using the version declared in `version.properties`.

Each release includes:

- `solventa.apk`, ready to install on Android.
- `solventa.apk.sha256`, for integrity verification.
- Automatically generated release notes and the version history.

Before merging a `release/<version>` or `hotfix/<name>` branch into `main`, increment both `VERSION_CODE` and `VERSION_NAME` in `version.properties`. A version already published cannot be reused. The permanent download link at the top of this README always points to the latest release.

### Install the APK

1. Open the [latest release page](https://github.com/JUANES545/solventa-app/releases/latest) on the Android phone.
2. Download `solventa.apk`.
3. If Android requests it, allow the browser or file manager to install unknown apps.
4. Open the downloaded file and confirm the installation.

Android may require uninstalling an older debug build before installing the first signed release because both builds use different signing certificates. Subsequent signed releases can update the installed release normally.

## Test scenarios

Debug builds include a **Test scenarios** panel on the login and profile screens. It controls fake repositories for normal data, empty lists, network error, slow response, expired session, successful claim submission, and failed claim submission. The entry point is not exposed in release builds.

## Future API integration

HTTP clients should implement the existing `AuthRepository` and `InsuranceRepository` contracts and be bound through Hilt in place of the fake implementations. Transport DTOs and mapping belong in the data layer; screens and ViewModels should remain unchanged.

## Status

Functional Android prototype under active development with GitFlow. Production services and real financial or insurance operations remain out of scope.

## Specifications

The product, navigation, architecture, design, quality, and Git workflow decisions are maintained in [docs/specs](docs/specs/README.md).
