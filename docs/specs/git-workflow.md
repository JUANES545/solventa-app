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

Use semantic versions beginning with the first demonstrable prototype, for example `v0.1.0`. Tags are created from `main` after verification. The exact academic submission version is documented in the release notes.
