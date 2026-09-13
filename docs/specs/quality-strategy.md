# Quality Strategy

## Test layers

- **Unit tests:** ViewModel state transitions, validation, filtering, fake scenarios, and formatting rules.
- **Compose UI tests:** demo authentication, complete travel quote-to-claim flow, new claim submission, bottom navigation, language switching, and theme switching.
- **Manual exploratory checks:** TalkBack, 200% font scale, keyboard behavior, back navigation, Photo Picker, location permission granted/denied, emulator location, empty/error scenarios, and light/dark contrast.

## Required scenarios

Before a feature is considered complete, verify its normal, loading, empty, recoverable error, and success states where applicable. Critical forms must cover invalid and interrupted submission paths.

## Definition of done

- Acceptance criteria in the relevant spec are implemented.
- Primary actions have behavior; no empty screens or unfinished `TODO` markers remain.
- Spanish and English resources are complete.
- Accessibility semantics and large-font layout are reviewed.
- Unit and applicable UI tests pass.
- Debug and release builds compile.
- No secrets, generated builds, IDE state, or real personal data are committed.
- README or specifications are updated when behavior or setup changes.
- The pull request targets `develop` and receives review before merge.

## Verification commands

The final commands will be confirmed when the Android project is generated. The expected baseline is:

```powershell
./gradlew.bat lintDebug
./gradlew.bat testDebugUnitTest
./gradlew.bat connectedDebugAndroidTest
./gradlew.bat assembleDebug
```

Connected tests require an emulator or device and should be reported separately when unavailable.

## Continuous verification

Pull requests to `develop` or `main`, and pushes to `develop`, run unit tests, lint, and compile the debug app and instrumentation tests. A merge into `main` additionally runs the navigation smoke tests on a Gradle Managed Device before signing and publishing the APK. No release may be created if any required verification fails.

Release automation also verifies the APK signature and confirms that its `versionCode` and `versionName` match `version.properties` before publication.

## Prototype review checklist

- A new evaluator can open and run the project using README instructions.
- All routes are reachable without hidden gestures.
- Fake data remains coherent across Home, Policies, Claims, and Notifications.
- The demonstration can be completed without network access.
- Simulated operations are visibly identified and do not mislead the user.
- The release build contains no route or entry point for the Test scenarios panel.
- The main activity remains portrait while layouts work across supported phone sizes and font scales.
