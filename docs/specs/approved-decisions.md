# Approved Decisions

These decisions resolve the initial specification questions and are binding for the prototype unless changed through a later specification update.

## Product journey

Travel insurance is the complete vertical journey. Products are travel, life, device, and parametric insurance. The sample **International Travel** policy connects issuance, policy detail, and claim reporting.

## Authentication

The fixed demo account is Álvaro Mejía, using `alvaro.mejia@solventa.co` / `Solventa123`. The email is prefilled at sign-in, and a separate **Enter as test user** action is available. Registration starts with coherent sample identity data so a moderated test can continue without typing personal information. Authentication is simulated behind a repository interface.

## Date input

All editable dates use the Android calendar picker instead of free-form text. Travel dates prevent past departures and returns before departure; claim event dates prevent future selection.

## Android integrations

Evidence selection uses Android Photo Picker with preview and removal. Location uses the device location after runtime permission; denial supports manual entry or a demo location. Neither evidence nor claim data is uploaded.

## Test scenarios

A debug-only panel configures fake repositories for normal data, empty lists, network error, slow response, expired session, successful claim, and failed claim. Release builds do not expose or include the panel entry point.

## Appearance

The app supports system, light, and dark appearance modes. Light is the primary visual presentation; system is the initial preference. Status is never communicated by color alone.

## Device target

The prototype targets phones and locks the main activity to portrait. It supports varying phone sizes, densities, and increased font scale. Tablet, landscape, and foldable-specific interfaces are out of scope.
