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
| Claims | Claim list/detail, affected policy, event description, evidence, location, submission | Device picker may be used; no upload occurs |
| Notifications | Payment, expiration, claim status, adjuster, nearby provider | Local read/unread state |
| Profile | Customer data, notification preferences, language, accessibility, logout | Local settings for the prototype session |

## Insurance products

The prototype may display vehicle, home, and life products. One product should provide the complete quote-to-issuance demonstration flow; the remaining products may reuse the flow with adjusted questions and sample coverage. The product selected for the complete flow remains an approval decision.

## Simulation contract

Every primary action must produce a visible result. Simulations must never imply that a real payment, identity verification, emergency call, policy issuance, cancellation, or claim submission occurred. No real endpoints, secrets, or provider credentials are permitted.

## Definition of the prototype boundary

The mobile app represents the future client of `Mobile App → API Gateway → Mobile BFF → Domain Services`. The BFF is not embedded in the app. Repository interfaces form the replacement boundary between fake data and future HTTP clients.
