# Open Decisions

The following decisions should be approved before Android implementation begins.

## 1. Complete quote product

Choose the product used for the full demonstration from quote through issued policy:

- Vehicle insurance — recommended because it also creates a natural claim-reporting scenario.
- Home insurance.
- Life insurance.

Other products remain visible with credible sample data but may reuse a shorter flow.

## 2. Authentication strictness

Choose whether exploratory testers may enter any valid-looking email/password or must use one documented demo account. A documented demo account makes the recorded demonstration more repeatable.

## 3. Device integrations

Confirm whether evidence selection and location should invoke real Android pickers/permissions while keeping submission simulated, or whether the entire interaction should remain an in-app simulation. Real system contracts improve usability testing but add permission and emulator setup work.

## 4. Scenario controls

Choose where testers activate empty and error states:

- A debug-only scenario panel — recommended for reliable exploratory testing.
- Separate documented demo accounts.
- A deterministic sequence of repeated actions.

## 5. Dark-theme scope

The current decision is a premium corporate dark theme. Confirm whether the prototype is dark-only or must also include a complete light theme. Supporting both correctly increases design and testing scope.

## 6. Supported orientation

The recommended prototype target is portrait phone layouts, while remaining safe during configuration changes. Tablet-specific layouts and landscape optimization are deferred unless required by the course rubric.

## Recorded resolution

The earlier product direction referenced light backgrounds. The latest explicit visual direction replaces it with the premium corporate dark theme defined in `design-system.md`. This is treated as an intentional change, not an unresolved contradiction.
