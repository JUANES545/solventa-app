package co.solventa.mobile.data

import co.solventa.mobile.domain.*
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAuthRepository @Inject constructor(
    private val scenarios: TestScenarioController
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        simulateDelay(scenarios.scenario.value)
        when (scenarios.scenario.value) {
            TestScenario.NETWORK_ERROR -> throw SimulatedNetworkException()
            TestScenario.SESSION_EXPIRED -> throw SessionExpiredException()
            else -> if (email != DEMO_EMAIL || password != DEMO_PASSWORD) throw InvalidCredentialsException()
        }
    }

    companion object {
        const val DEMO_EMAIL = "demo@solventa.co"
        const val DEMO_PASSWORD = "Solventa123"
    }
}

@Singleton
class FakeInsuranceRepository @Inject constructor(
    private val scenarios: TestScenarioController
) : InsuranceRepository {
    private val submittedClaims = mutableListOf<Claim>()
    override suspend fun policies(): List<Policy> {
        beforeRequest()
        if (scenarios.scenario.value == TestScenario.EMPTY_LISTS) return emptyList()
        return samplePolicies
    }

    override suspend fun claims(): List<Claim> {
        beforeRequest()
        if (scenarios.scenario.value == TestScenario.EMPTY_LISTS) return emptyList()
        return submittedClaims + sampleClaims
    }

    override suspend fun notifications(): List<SolventaNotification> {
        beforeRequest()
        if (scenarios.scenario.value == TestScenario.EMPTY_LISTS) return emptyList()
        return sampleNotifications
    }

    override suspend fun travelPlans(details: TravelDetails): List<TravelPlan> {
        beforeRequest()
        return listOf(
            TravelPlan("essential", PlanLevel.ESSENTIAL, 89_900, 80_000_000, 2_000_000),
            TravelPlan("plus", PlanLevel.PLUS, 139_900, 150_000_000, 4_000_000),
            TravelPlan("premium", PlanLevel.PREMIUM, 219_900, 300_000_000, 8_000_000)
        )
    }

    override suspend fun issueTravelPolicy(plan: TravelPlan): Policy {
        beforeRequest()
        return samplePolicies.first()
    }

    override suspend fun submitClaim(draft: ClaimDraft): Claim {
        beforeRequest()
        if (scenarios.scenario.value == TestScenario.CLAIM_ERROR) throw ClaimSubmissionException()
        return Claim(
            id = "SIN-2026-1058",
            policyId = draft.policyId,
            type = draft.type,
            status = ClaimStatus.SUBMITTED,
            eventDate = draft.eventDate.ifBlank { "2026-09-09" },
            description = draft.description
        ).also { submittedClaims.add(0, it) }
    }

    private suspend fun beforeRequest() {
        simulateDelay(scenarios.scenario.value)
        when (scenarios.scenario.value) {
            TestScenario.NETWORK_ERROR -> throw SimulatedNetworkException()
            TestScenario.SESSION_EXPIRED -> throw SessionExpiredException()
            else -> Unit
        }
    }

    companion object {
        val samplePolicies = listOf(
            Policy("SOL-TRV-2026-1842", InsuranceProduct.TRAVEL, PolicyStatus.ACTIVE, 139_900, "2026-10-04", "2026-10-19", listOf("medical", "baggage", "delay")),
            Policy("SOL-LIF-2026-0714", InsuranceProduct.LIFE, PolicyStatus.ACTIVE, 64_500, "2026-01-01", "2027-01-01", listOf("life", "disability")),
            Policy("SOL-DEV-2025-0280", InsuranceProduct.DEVICE, PolicyStatus.EXPIRING, 22_900, "2025-10-01", "2026-10-01", listOf("damage", "theft")),
            Policy("SOL-PAR-2025-0131", InsuranceProduct.PARAMETRIC, PolicyStatus.EXPIRED, 31_000, "2025-01-01", "2026-01-01", listOf("rainfall"))
        )
        val sampleClaims = listOf(
            Claim("SIN-2026-0942", "SOL-TRV-2026-1842", ClaimType.DELAYED_FLIGHT, ClaimStatus.IN_REVIEW, "2026-08-18", "Flight delay during international travel"),
            Claim("SIN-2026-0811", "SOL-TRV-2026-1842", ClaimType.LOST_BAGGAGE, ClaimStatus.APPROVED, "2026-05-12", "Baggage did not arrive at destination")
        )
        val sampleNotifications = listOf(
            SolventaNotification("NOT-1", NotificationType.CLAIM_UPDATE, false, "2026-09-08"),
            SolventaNotification("NOT-2", NotificationType.PAYMENT, false, "2026-09-05"),
            SolventaNotification("NOT-3", NotificationType.EXPIRATION, true, "2026-09-01")
        )
    }
}

private suspend fun simulateDelay(scenario: TestScenario) {
    delay(if (scenario == TestScenario.SLOW_RESPONSE) 2_500 else 450)
}
