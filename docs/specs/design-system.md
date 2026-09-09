# Design System and Accessibility

## Visual direction

Solventa uses a premium corporate dark theme: deep navy creates trust and financial seriousness, while electric cyan is reserved for high-value actions. The interface must remain calm and readable rather than appearing like a gaming or cryptocurrency product.

This direction supersedes the earlier light-background suggestion for the mobile prototype. A light theme is not assumed unless it is approved as additional scope.

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

Colors are semantic tokens, not values written directly inside feature composables. A light scheme may be designed later, but the prototype target is the dark scheme.

## Usage rules

- Electric cyan is limited to primary conversion actions such as Quote, Buy Insurance, Download Policy, or Call Emergency Assistance.
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
