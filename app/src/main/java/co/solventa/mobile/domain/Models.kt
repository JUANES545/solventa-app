package co.solventa.mobile.domain

import android.net.Uri

enum class InsuranceProduct { TRAVEL, LIFE, DEVICE, PARAMETRIC }
enum class PolicyStatus { ACTIVE, EXPIRING, EXPIRED }
enum class ClaimStatus { SUBMITTED, IN_REVIEW, APPROVED, CLOSED }
enum class ClaimType { DELAYED_FLIGHT, LOST_BAGGAGE, MEDICAL_ASSISTANCE }
enum class NotificationType { PAYMENT, EXPIRATION, CLAIM_UPDATE, ADJUSTER }

data class Policy(
    val id: String,
    val product: InsuranceProduct,
    val status: PolicyStatus,
    val premiumCop: Long,
    val validFrom: String,
    val validUntil: String,
    val coverage: List<String>
)

data class Claim(
    val id: String,
    val policyId: String,
    val type: ClaimType,
    val status: ClaimStatus,
    val eventDate: String,
    val description: String
)

data class SolventaNotification(
    val id: String,
    val type: NotificationType,
    val read: Boolean,
    val createdAt: String
)

data class TravelDetails(
    val destination: String = "",
    val departureDate: String = "",
    val returnDate: String = "",
    val travelers: Int = 1
)

data class TravelPlan(
    val id: String,
    val level: PlanLevel,
    val priceCop: Long,
    val medicalCoverageCop: Long,
    val baggageCoverageCop: Long
)

enum class PlanLevel { ESSENTIAL, PLUS, PREMIUM }

data class ClaimDraft(
    val policyId: String = "SOL-TRV-2026-1842",
    val type: ClaimType = ClaimType.DELAYED_FLIGHT,
    val eventDate: String = "",
    val description: String = "",
    val evidence: List<Uri> = emptyList(),
    val location: String = ""
)

sealed interface LoadState<out T> {
    data object Idle : LoadState<Nothing>
    data object Loading : LoadState<Nothing>
    data class Content<T>(val data: T) : LoadState<T>
    data object Empty : LoadState<Nothing>
    data class Error(val kind: ErrorKind) : LoadState<Nothing>
}

enum class ErrorKind { NETWORK, SESSION_EXPIRED, SUBMISSION, UNKNOWN }
