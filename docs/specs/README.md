# Solventa Mobile Specifications

These documents define the agreed foundation for the Solventa Android prototype before implementation begins.

## Specification index

1. [Product scope](product-scope.md)
2. [Screen catalog](screen-catalog.md)
3. [Navigation and UI states](navigation-and-states.md)
4. [Android architecture](android-architecture.md)
5. [Design system and accessibility](design-system.md)
6. [Quality strategy](quality-strategy.md)
7. [Git workflow](git-workflow.md)
8. [Open decisions](open-decisions.md)

## Status and change rules

- Current status: **Draft for review**.
- The specifications describe the prototype target, not production backend behavior.
- A material scope or architecture change must update the affected specification in the same pull request as the implementation.
- When documents disagree, the latest explicitly approved decision takes precedence. The contradiction must be recorded rather than silently resolved in code.
- Implementation must not begin until the initial specification set is approved and merged into `develop`.

## Out of scope for this phase

- Production APIs, AWS infrastructure, API Gateway, or a Mobile BFF implementation.
- Real KYC, biometric authentication, payment processing, Open Finance, GPS tracking, or document signing.
- Administrative and partner-facing functionality.
- Production security certification or regulatory approval.
