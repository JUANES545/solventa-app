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
8. [Approved decisions](approved-decisions.md)

## Status and change rules

- Current status: **Approved foundation**.
- The specifications describe the prototype target, not production backend behavior.
- A material scope or architecture change must update the affected specification in the same pull request as the implementation.
- When documents disagree, the latest explicitly approved decision takes precedence. The contradiction must be recorded rather than silently resolved in code.
- Implementation may begin after this approved specification set is merged into `develop`.

## Out of scope for this phase

- Production APIs, AWS infrastructure, API Gateway, or a Mobile BFF implementation.
- Real KYC, biometric authentication, payment processing, Open Finance, file upload, or document signing. Photo selection and device location retrieval are real local Android interactions, but their final submission remains simulated.
- Administrative and partner-facing functionality.
- Production security certification or regulatory approval.
