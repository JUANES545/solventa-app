# Design System and Accessibility

## Visual direction

Solventa supports complete light and dark themes. The light theme is the primary presentation and preserves the existing identity of clear backgrounds, dark blue, green confirmations, and red alerts. The dark theme uses the premium corporate direction: deep navy creates trust and financial seriousness, while electric cyan carries primary and high-value actions with sufficient contrast. The initial preference follows the Android system setting, and the customer may select system, light, or dark in Settings.

## Brand identity

The Solventa symbol combines a shield with an `S`. The shield represents protection and trust. The continuous `S` identifies Solventa without resembling a currency sign. Web, Android, documents, and design files must use the same geometry.

- Light surfaces: Corporate Navy `#123B6D` shield with White `#FFFFFF` letter.
- Dark surfaces: Electric Cyan `#00D2FF` shield with Deep Ink `#001018` letter.
- Minimum digital size: `16 dp`.
- Standard Welcome size: `88 dp`; compact access size: `48 dp`.
- Clear space: at least one quarter of the symbol width on every side.
- Do not stretch, rotate, outline, recolor, or add effects to the symbol.
- Pair the symbol with the `Solventa` name when space permits. Symbol-only controls require an accessible name.

The reusable Compose source is `core/ui/BrandMark.kt`. Adaptive and monochrome launcher artwork uses the same geometry from Android resources.

## Typography

Solventa uses the same font families on web and Android:

- **Geologica Variable:** brand, display, headline, and title roles.
- **Afacad Flux Variable:** body, label, input, button, navigation, and metadata roles.
- **Platform monospace:** optional for technical and audit displays. Customer-facing references remain in Afacad Flux.

Android preserves the Material 3 `sp` type scale and replaces only the font families. Web keeps its responsive `rem` scale. This aligns identity and hierarchy while preserving native text scaling. Font files are distributed under the SIL Open Font License in `docs/licenses`.

## Dark color tokens

| Token | Color | Hex | Intended use |
| --- | --- | --- | --- |
| `background` | Abyss Navy | `#0B0F19` | Main background, approximately 60% of the interface |
| `surface` | Slate Navy | `#1E2640` | Cards, sheets, dialogs, and policy containers |
| `outline` | Metallic Blue Gray | `#71829C` | Main input and component borders |
| `outlineSoft` | Deep Slate | `#334155` | Dividers and subtle borders |
| `onBackground` | Pure White | `#FFFFFF` | Primary text |
| `onSurfaceVariant` | Cool Light Gray | `#CBD5E1` | Secondary text and supporting metadata |
| `primary` | Electric Cyan | `#00D2FF` | Standard primary actions and navigation in dark mode |
| `conversion` | Electric Cyan | `#00D2FF` | High-value conversion and urgent assistance actions |
| `onPrimary` | Deep Ink | `#001018` | Text/icons displayed on cyan |
| `success` | Emerald | `#10B981` | Active policy, approved claim, successful payment |
| `warning` | Insurance Gold | `#F59E0B` | Expiration, review required, pending attention |
| `error` | Accessible Red | `#FF8A80` | Validation errors, failed operations, critical claim alerts |

Colors are semantic tokens, not values written directly inside feature composables. Both complete schemes are required, with the light scheme serving as the primary visual reference.

## Light color tokens

| Token | Color | Hex | Intended use |
| --- | --- | --- | --- |
| `background` | Clear Ice | `#F7F9FC` | Primary light background |
| `surface` | Pure White | `#FFFFFF` | Cards, sheets, and inputs |
| `surfaceVariant` | Mist Blue | `#E8EEF6` | Grouping and secondary containers |
| `outline` | Steel Blue Gray | `#64748B` | Input and component borders |
| `onBackground` | Deep Navy | `#0B1F3A` | Primary text |
| `onSurfaceVariant` | Slate | `#475569` | Secondary text |
| `primary` | Corporate Navy | `#123B6D` | Standard primary actions and navigation |
| `conversion` | Accessible Cyan Blue | `#007FA3` | High-value conversion actions on light surfaces |
| `onPrimary` | Pure White | `#FFFFFF` | Text/icons on navy and accessible cyan buttons |
| `success` | Deep Emerald | `#047857` | Positive status and confirmation |
| `warning` | Deep Amber | `#A44708` | Expiration and attention states |
| `error` | Insurance Red | `#B42318` | Errors, destructive actions, and critical alerts |

All foreground/background pairings must be verified rather than assuming the same semantic shade works in both themes.

## Usage rules

- In the light theme, cyan is limited to high-value conversion actions such as Quote, Buy Insurance, Download Policy, or Call Emergency Assistance.
- In the dark theme, cyan provides sufficient contrast for both primary and conversion actions. It must not become general-purpose text or status color.
- Focused inputs change from `outline` to `primary`; focus also remains identifiable through stroke and label changes.
- Success, warning, and error colors communicate status together with text and/or iconography.
- Gradients are optional and limited to selected linear icons or small brand accents. They must not reduce icon clarity.
- Claim reporting itself is not always an error. Red is reserved for critical status, destructive action, or immediate danger.

## Components

- Minimum interactive target: 48 × 48 dp.
- Cards show policy/claim identity, status text, and one clear next action.
- Long quote forms use short grouped steps, progress indication, suitable keyboards, and inline validation.
- Destructive actions require confirmation and visually differ from the primary CTA.
- Loading uses skeletons or progress indicators without causing layout jumps.

## Shared spacing and shape roles

The shared spacing scale is `4, 8, 12, 16, 20, 24, 28, 32, 40, 48, 64`. Android maps these values to `dp`; web maps them to `px` and may use fluid spacing between breakpoints. Existing screens can retain intermediate values while components migrate to this scale.

- Radius roles: small, medium, large, and pill.
- Elevation roles: flat, raised, and overlay. Android uses Material elevation; web uses CSS shadows.
- Minimum interactive target: `48 × 48 dp` on Android and `48 × 48 px` on web.

## Grid and layout

Android currently supports phone portrait. Standard screen padding is `20dp` horizontal and `16dp` vertical. Standard page sections use a `16dp` gap, while repeated list items use a compact `12dp` gap. Primary navigation uses five bottom destinations. Tablet, landscape, and foldable layouts remain outside the prototype scope.

Web uses responsive side navigation and grids with breakpoints at `680px`, `920px`, and `1320px`. These platform layouts share content hierarchy but are not forced into identical grids.

## State contract

Interactive components support default, focused, pressed, selected, disabled, loading, and invalid states where applicable. Hover is web-only; Android uses native Material ripple and focus behavior.

Repository-backed screens support initial, loading, content, empty, recoverable error, and success. Forms support initial, editing, invalid, submitting, and submitted. Empty states explain what happened and provide a useful action where applicable. Recoverable errors provide retry.

## Accessibility acceptance criteria

- Normal text and controls meet WCAG AA contrast targets; large text is checked separately.
- The app remains usable at Android font scale 200% without clipped primary actions or hidden information.
- TalkBack receives meaningful labels, roles, state, and traversal order.
- Decorative graphics are excluded from the accessibility tree.
- Selected, expanded, read, pending, success, and error states are exposed semantically.
- Authentication and form errors are announced and explain the correction.
- Motion is brief, purposeful, and respects reduced-motion platform settings where applicable.

## Copy tone

Use calm, direct language. Explain what happened, whether the action is simulated, and what the customer can do next. Avoid legal or technical jargon unless required, and never use alarming language for recoverable errors.
