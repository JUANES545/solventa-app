# Solventa Mobile App

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

- Email: `demo@solventa.co`
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

## Test scenarios

Debug builds include a **Test scenarios** panel on the login and profile screens. It controls fake repositories for normal data, empty lists, network error, slow response, expired session, successful claim submission, and failed claim submission. The entry point is not exposed in release builds.

## Future API integration

HTTP clients should implement the existing `AuthRepository` and `InsuranceRepository` contracts and be bound through Hilt in place of the fake implementations. Transport DTOs and mapping belong in the data layer; screens and ViewModels should remain unchanged.

## Status

Functional Android prototype on the `feature/android-prototype` branch. Production services and real financial or insurance operations remain out of scope.

## Specifications

The product, navigation, architecture, design, quality, and Git workflow decisions are maintained in [docs/specs](docs/specs/README.md).
