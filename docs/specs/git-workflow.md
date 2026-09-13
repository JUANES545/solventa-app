# Git Workflow

## Branch model

Solventa uses a lightweight GitFlow model:

| Branch | Purpose | Merge destination |
| --- | --- | --- |
| `main` | Stable, demonstrable releases | — |
| `develop` | Integrated work for the next release | `main` through a release PR |
| `feature/<name>` | One bounded feature or documentation change | `develop` |
| `release/<version>` | Final stabilization only | `main`, then back to `develop` |
| `hotfix/<name>` | Urgent correction to a released version | `main`, then back to `develop` |

Long-lived work happens only in `main` and `develop`. Feature branches should be short-lived.

## Naming

Use lowercase kebab-case and descriptive names, for example:

- `feature/authentication-flow`
- `feature/quote-prototype`
- `fix/claim-form-validation`
- `release/0.1.0`

## Commit rules

- Write concise imperative messages in English.
- Keep commits cohesive and independently understandable.
- Do not include secrets, generated artifacts, personal IDE configuration, or references to automated authorship.
- Do not rewrite shared branch history.
- Repository commits use `Juan Mejía <40007217+JUANES545@users.noreply.github.com>`.

Recommended prefixes are `feat:`, `fix:`, `docs:`, `test:`, `refactor:`, `build:`, and `chore:`. They are a convention, not a replacement for a clear message.

## Pull requests

- Features and fixes target `develop`.
- Release and hotfix pull requests target `main`.
- A pull request explains the user-visible outcome, technical decisions, tests performed, and known limitations.
- Relevant specifications change in the same pull request when behavior changes.
- `main` and `develop` should be protected from direct pushes once collaboration or grading begins.

## Releases

Use semantic versions beginning with the first demonstrable prototype, for example `v0.1.0`. The version source is the root `version.properties` file:

- `VERSION_CODE` is a positive Android build number and must increase for every release.
- `VERSION_NAME` is the public semantic version without the `v` prefix.

Update both values in the corresponding `release/<version>` or `hotfix/<name>` branch before merging it into `main`. Versions must never be reused.

Every push to `main` starts the Android Release workflow. It runs unit tests, lint, instrumentation smoke tests on a Gradle Managed Device, builds and verifies a signed APK, and then creates the `v<VERSION_NAME>` tag and GitHub Release. The release publishes `solventa.apk` and its SHA-256 checksum. A failed verification must stop publication.

Signing material exists only in the GitHub Actions secrets and in the maintainer's protected local backup. Keystores, passwords, reconstructed secret files, and generated APKs must never be committed. The exact academic submission version is documented in the release notes.
