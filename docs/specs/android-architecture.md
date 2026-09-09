# Android Architecture

## Principles

- Prefer clarity over framework ceremony.
- Organize code by feature, with shared foundations kept small.
- Keep composables declarative and free of repository or network access.
- Depend on interfaces at data boundaries.
- Model expected failures as UI state rather than exceptions reaching the UI.
- Avoid use cases that only forward a repository call; introduce them when they contain reusable business rules.

## Technology baseline

- Kotlin and Android Studio
- Jetpack Compose and Material 3
- Navigation Compose
- ViewModel, StateFlow, and coroutines
- Hilt dependency injection
- Gradle version catalog
- Retrofit only when the Mobile BFF contract exists

Exact dependency versions will be selected at project creation and recorded in the version catalog. Stable releases are preferred unless a documented requirement needs a preview version.

## Proposed modules

Start with a single `app` module to keep the academic prototype manageable. Package by feature:

```text
app/
├── core/
│   ├── designsystem/
│   ├── navigation/
│   ├── model/
│   └── testing/
├── feature/
│   ├── auth/
│   ├── onboarding/
│   ├── home/
│   ├── quote/
│   ├── policies/
│   ├── claims/
│   ├── notifications/
│   └── profile/
└── data/
    ├── repository/
    ├── fake/
    └── remote/       # Added only when an API contract exists
```

Additional Gradle modules are justified only by measured build, ownership, or reuse needs.

## Feature flow

```text
Composable → ViewModel → Repository interface → Fake implementation
                                      └───────→ Future HTTP implementation
```

The UI observes an immutable `UiState` through `StateFlow` and emits typed user actions. A ViewModel coordinates validation, calls, and state transitions. Domain models are independent of future transport DTOs; mapping occurs in the data layer.

## State conventions

- One immutable state object per screen or cohesive flow.
- Events are functions or typed actions, not a generic string event bus.
- One-off effects are reserved for navigation, permission launchers, and external intents.
- Coroutines run in `viewModelScope`; dispatchers are injectable when needed for tests.
- Fake delays and scenario selection live in fake data sources, never in composables.

## Data and security

- Fake data must contain fictional identities and payment details.
- Secrets and local SDK paths must never enter Git.
- Logs must not include passwords, OTPs, document numbers, tokens, or consent payloads.
- Sensitive fields use appropriate keyboard and visual-transformation settings.
- Runtime permissions are requested only at the moment a related action is selected, with a clear rationale and denial path.

## Localization

- Spanish is the default; English coverage is complete.
- Customer-facing copy lives in string resources, including accessibility and error text.
- Dates, times, numbers, and COP amounts use locale-aware formatters.
- Language selection uses Android-supported application locales and persists across launches.
