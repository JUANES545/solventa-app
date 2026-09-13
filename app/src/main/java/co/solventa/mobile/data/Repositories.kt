package co.solventa.mobile.data

import co.solventa.mobile.domain.*

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
}

interface InsuranceRepository {
    suspend fun policies(): List<Policy>
    suspend fun claims(): List<Claim>
    suspend fun notifications(): List<SolventaNotification>
    suspend fun travelPlans(details: TravelDetails): List<TravelPlan>
    suspend fun issueTravelPolicy(plan: TravelPlan): Policy
    suspend fun submitClaim(draft: ClaimDraft): Claim
}

class InvalidCredentialsException : Exception()
class SimulatedNetworkException : Exception()
class SessionExpiredException : Exception()
class ClaimSubmissionException : Exception()
