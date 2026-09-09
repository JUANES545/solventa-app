# Product Scope

## Objective

Build a navigable Android prototype for an exploratory user test and a short recorded demonstration. It should feel credible as a Colombian insurance application while using controlled simulated data.

## Target user

An authenticated customer or policyholder who wants to quote, purchase, and manage insurance, report a claim, and review notifications.

## Functional areas

| Area | Included flow | Prototype behavior |
| --- | --- | --- |
| Access | Welcome, login, password recovery, simulated biometric entry | Local validation and visible feedback |
| Registration | Basic information, terms, data processing, Open Finance consent | Consent choices retained during the flow |
| Identity | Document capture, selfie/liveness, result | Guided simulation with success and recoverable error |
| Home | Active policies, quick actions, recent notifications, upcoming payments | Aggregated fake repository data |
| Quote | Product selection, relevant questions, consent, calculation, plan comparison | Delayed calculation with deterministic results |
| Purchase | Quote summary, payment method, data confirmation, OTP, policy issuance | Simulated payment and issuance confirmation |
| Policies | Status filters, details, coverage, validity, premium, document, renewal/cancellation | Simulated actions with visible confirmation |
| Claims | Claim list/detail, affected policy, event description, Photo Picker evidence, device/manual location, submission | Local device interactions; no upload occurs |
| Notifications | Payment, expiration, claim status, adjuster, nearby provider | Local read/unread state |
| Profile | Customer data, notification preferences, language, accessibility, logout | Local settings for the prototype session |

## Insurance products

The current product catalog is travel, life, device, and parametric insurance. Travel insurance provides the complete demonstration flow:

`Quote → Plan selection → Consent → Simulated payment → Simulated OTP signature → Issuance → Policy detail → Claim report`

The other products may appear through coherent simulated catalog, quote, policy, and notification data, but do not require the same interaction depth.

The issued sample travel policy is named **International Travel**. Claim history and the new-claim flow must use the same policy so cross-screen data remains coherent.

## Simulation contract

Every primary action must produce a visible result. Simulations must never imply that a real payment, identity verification, emergency call, policy issuance, cancellation, or claim submission occurred. No real endpoints, secrets, or provider credentials are permitted.

## Demo account

- Email: `demo@solventa.co`
- Password: `Solventa123`

Login also provides an **Enter as test user** action. Authentication is handled by a fake repository and covers incomplete input, incorrect credentials, loading, success, and recoverable login error.

## Definition of the prototype boundary

The mobile app represents the future client of `Mobile App → API Gateway → Mobile BFF → Domain Services`. The BFF is not embedded in the app. Repository interfaces form the replacement boundary between fake data and future HTTP clients.
