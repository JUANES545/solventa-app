package co.solventa.mobile.feature.claims

import co.solventa.mobile.MainDispatcherRule
import co.solventa.mobile.data.*
import co.solventa.mobile.domain.ClaimType
import co.solventa.mobile.domain.LoadState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ClaimViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    @Test fun configuredFailureProducesRecoverableError() = runTest {
        val scenarios = TestScenarioController().apply { select(TestScenario.CLAIM_ERROR) }
        val viewModel = ClaimViewModel(FakeInsuranceRepository(scenarios))
        viewModel.setEvent("2026-09-09", "Delayed flight", ClaimType.DELAYED_FLIGHT)
        viewModel.setLocation("Bogotá"); viewModel.submit(); advanceUntilIdle()
        assertTrue(viewModel.state.value.submission is LoadState.Error)
    }

    @Test fun normalSubmissionReturnsClaimReference() = runTest {
        val viewModel = ClaimViewModel(FakeInsuranceRepository(TestScenarioController()))
        viewModel.setEvent("2026-09-09", "Delayed flight", ClaimType.DELAYED_FLIGHT)
        viewModel.setLocation("Bogotá"); viewModel.submit(); advanceUntilIdle()
        assertTrue(viewModel.state.value.submission is LoadState.Content)
    }
}
