# Navigation and UI States

## Navigation model

Authentication and onboarding are separate from the signed-in area. The signed-in area uses bottom navigation with five destinations: Home, Policies, Claims, Notifications, and Profile.

Secondary routes are pushed above the selected destination:

```text
Welcome
├── Login
│   ├── Password recovery
│   └── Simulated biometric access
└── Registration → Consents → KYC document → KYC selfie → KYC result

Main
├── Home
│   ├── Quote → Calculation → Plans → Purchase → OTP → Issued policy
│   └── New claim → Confirmation
├── Policies → Policy detail → Simulated action result
├── Claims → Claim detail / New claim → Confirmation
├── Notifications → Notification detail
└── Profile → Preferences / Language / Accessibility
```

## Navigation rules

- Back returns to the previous logical step and never exits a multi-step form without warning when data was entered.
- Selecting the current bottom destination does not create a duplicate route.
- Each bottom destination retains scroll position and filters where practical.
- Completing login or registration clears onboarding from the back stack.
- Completing logout clears all authenticated routes and session-only data.
- Deep links and partner routes are not required for the prototype.

## Required UI states

Every repository-backed screen must support:

- **Loading:** short simulated delay and non-blocking progress feedback.
- **Content:** realistic, coherent Colombian insurance data.
- **Empty:** explanation plus a useful next action where applicable.
- **Recoverable error:** plain-language message and retry action.
- **Success:** confirmation for completed simulated operations.

Forms additionally support initial, editing, invalid, submitting, and submitted states. Error messages must identify how to correct the value and must not rely only on color.

## Demo controls

Empty and error examples must be deterministic and easy to activate for exploratory testing. The mechanism may be a debug-only scenario selector or injected fake repository configuration; it must not appear as a production customer feature.

## State preservation

- Screen state belongs in a ViewModel when it must survive recomposition and configuration change.
- Temporary visual state may remain inside a composable.
- Saved state is used for small navigation arguments and unfinished form identifiers, not for entire domain objects.
