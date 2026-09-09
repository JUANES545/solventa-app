# Screen Catalog

This catalog defines the planned screens and meaningful prototype behavior. Minor dialogs and permission prompts do not require standalone routes.

| ID | Screen | Primary actions | Required special states |
| --- | --- | --- | --- |
| AUTH-01 | Welcome | Sign in, create account | Default |
| AUTH-02 | Sign in | Submit credentials, biometric entry, recover password, open registration | Invalid credentials, submitting, success |
| AUTH-03 | Password recovery | Submit email, return to sign in | Invalid email, sending, confirmation |
| REG-01 | Basic registration | Enter identity/contact data, continue | Validation, submitting |
| REG-02 | Terms and privacy | Open documents, accept or decline required terms | Required consent missing |
| REG-03 | Open Finance consent | Review purpose, authorize or skip where permitted | Authorized, declined |
| KYC-01 | Identity introduction | Start verification | Default |
| KYC-02 | Document capture | Select document type, capture front/back, retry | Permission denied, invalid capture, processing |
| KYC-03 | Selfie and liveness | Start simulated capture, retry | Permission denied, processing, recoverable failure |
| KYC-04 | Verification result | Finish registration or retry | Approved, pending, recoverable failure |
| HOME-01 | Home | Open quote, new claim, policy, payment, notification | Loading, content, partial empty, recoverable error |
| QUOTE-01 | Product selection | Select vehicle, home, or life insurance | Selection required |
| QUOTE-02 | Quote questions | Complete product-specific information, go back/continue | Validation, preserved draft |
| QUOTE-03 | Quote consent | Review and authorize Open Finance use | Authorized, declined alternative |
| QUOTE-04 | Calculation | Wait or cancel | Loading, recoverable error |
| QUOTE-05 | Quote results | Compare plans, view coverage, select a plan | Content, unavailable result, error |
| BUY-01 | Purchase summary | Review plan and customer data, continue | Default |
| BUY-02 | Payment method | Select/add simulated method, continue | Validation, simulated payment failure |
| BUY-03 | Confirmation | Confirm information and terms | Required confirmation missing |
| BUY-04 | OTP | Enter/resend simulated code | Invalid, expired, verified |
| BUY-05 | Issuance result | View issued policy, return home | Processing, issued, recoverable failure |
| POL-01 | Policy list | Filter by active, expiring, expired; open detail | Loading, content, empty, error |
| POL-02 | Policy detail | View coverage, download document, renew, cancel | Active, expiring, expired |
| POL-03 | Policy action result | Return to policy | Success or recoverable failure |
| CLM-01 | Claim list | Open claim, create report | Loading, content, empty, error |
| CLM-02 | Claim detail | Review timeline, adjuster, provider information | Pending, in review, approved, rejected, closed |
| CLM-03 | Select affected policy | Choose an eligible policy | Empty eligible-policy state |
| CLM-04 | Event information | Enter date, type, description | Validation, preserved draft |
| CLM-05 | Evidence | Use device picker, remove selected files | Permission/selection cancelled, size/type error |
| CLM-06 | Location | Use current location or enter manually | Permission denied, unavailable GPS |
| CLM-07 | Claim review | Edit sections, submit | Submitting, recoverable error |
| CLM-08 | Claim confirmation | View generated claim reference, return to claims | Success |
| NOT-01 | Notification list | Open, mark read, mark all read | Loading, content, empty, error |
| NOT-02 | Notification detail | Follow contextual action | Read/unread |
| PRO-01 | Profile | Review customer information | Content |
| PRO-02 | Notification preferences | Enable/disable categories | Saved feedback |
| PRO-03 | Language | Select Spanish or English | Current selection |
| PRO-04 | Accessibility | Enable larger UI text and reduced motion where supported | Current selection |
| PRO-05 | Logout confirmation | Cancel or end session | Default |

## Route granularity

Closely related registration, quote, purchase, and claim steps may share one composable host with an internal step model. Each step must still have a stable route or saved step identifier so back navigation and state restoration are predictable.

## Primary-action rule

Every action in the catalog must navigate, update visible state, launch an appropriate device contract, or display a clearly labeled simulation response. Placeholder buttons are not accepted.
