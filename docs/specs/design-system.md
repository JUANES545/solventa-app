# Design System and Accessibility

## Visual direction

Solventa supports complete light and dark themes. The light theme is the primary presentation and preserves the existing identity of clear backgrounds, dark blue, green confirmations, and red alerts. The dark theme uses the premium corporate direction: deep navy creates trust and financial seriousness, while electric cyan is reserved for high-value actions. The initial preference follows the Android system setting, and the customer may select system, light, or dark in Settings.

## Dark color tokens

| Token | Color | Hex | Intended use |
| --- | --- | --- | --- |
| `background` | Abyss Navy | `#0B0F19` | Main background, approximately 60% of the interface |
| `surface` | Slate Navy | `#1E2640` | Cards, sheets, dialogs, and policy containers |
| `outline` | Metallic Blue Gray | `#334155` | Input and component borders |
| `onBackground` | Pure White | `#FFFFFF` | Primary text |
| `onSurfaceVariant` | Cool Light Gray | `#CBD5E1` | Secondary text and supporting metadata |
| `primary` | Electric Cyan | `#00D2FF` | Conversion and urgent assistance actions only |
| `onPrimary` | Deep Ink | `#001018` | Text/icons displayed on cyan |
| `success` | Emerald | `#10B981` | Active policy, approved claim, successful payment |
| `warning` | Insurance Gold | `#F59E0B` | Expiration, review required, pending attention |
| `error` | Accessible Red | `#F87171` | Validation errors, failed operations, critical claim alerts |

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
| `warning` | Deep Amber | `#B45309` | Expiration and attention states |
| `error` | Insurance Red | `#B42318` | Errors, destructive actions, and critical alerts |

All foreground/background pairings must be verified rather than assuming the same semantic shade works in both themes.

## Usage rules

- Cyan is limited to high-value conversion actions such as Quote, Buy Insurance, Download Policy, or Call Emergency Assistance. The light theme uses its darker accessible variant for sufficient contrast.
- Cyan must not become a decorative background or general-purpose text color.
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
