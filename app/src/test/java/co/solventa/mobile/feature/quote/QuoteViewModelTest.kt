package co.solventa.mobile.feature.quote

import co.solventa.mobile.MainDispatcherRule
import co.solventa.mobile.data.FakeInsuranceRepository
import co.solventa.mobile.data.TestScenarioController
import co.solventa.mobile.domain.LoadState
import co.solventa.mobile.domain.TravelDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuoteViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    @Test fun completeTravelQuoteCanBeIssuedWithDemoOtp() = runTest {
        val viewModel = QuoteViewModel(FakeInsuranceRepository(TestScenarioController()))
        viewModel.setDetails("Madrid", "2026-10-04", "2026-10-19", 1)
        viewModel.loadPlans(); advanceUntilIdle()
        val plans = (viewModel.state.value.plans as LoadState.Content).data
        viewModel.selectPlan(plans[1]); viewModel.setConsent(true); viewModel.setPaymentSelected(true); viewModel.setOtp("123456")
        viewModel.issue(); advanceUntilIdle()
        assertEquals("SOL-TRV-2026-1842", viewModel.state.value.issuedPolicy?.id)
    }

    @Test fun invalidOtpDoesNotIssuePolicy() {
        val viewModel = QuoteViewModel(FakeInsuranceRepository(TestScenarioController()))
        viewModel.selectPlan(FakeInsuranceRepository(TestScenarioController()).let { co.solventa.mobile.domain.TravelPlan("x", co.solventa.mobile.domain.PlanLevel.ESSENTIAL, 1, 1, 1) })
        viewModel.setOtp("000000"); viewModel.issue()
        assertNull(viewModel.state.value.issuedPolicy)
        assertNotNull(viewModel.state.value.error)
    }
}
