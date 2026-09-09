package co.solventa.mobile.feature.auth

import co.solventa.mobile.MainDispatcherRule
import co.solventa.mobile.data.FakeAuthRepository
import co.solventa.mobile.data.TestScenarioController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    @Test fun incompleteCredentialsShowValidation() {
        val viewModel = AuthViewModel(FakeAuthRepository(TestScenarioController()))
        viewModel.login()
        assertEquals(AuthError.REQUIRED_FIELDS, viewModel.state.value.error)
    }

    @Test fun incorrectCredentialsAreRejected() = runTest {
        val viewModel = AuthViewModel(FakeAuthRepository(TestScenarioController()))
        viewModel.setEmail("wrong@solventa.co"); viewModel.setPassword("wrong"); viewModel.login()
        advanceUntilIdle()
        assertEquals(AuthError.INVALID_CREDENTIALS, viewModel.state.value.error)
        assertFalse(viewModel.state.value.authenticated)
    }

    @Test fun testUserCanAuthenticate() = runTest {
        val viewModel = AuthViewModel(FakeAuthRepository(TestScenarioController()))
        viewModel.enterAsTestUser(); advanceUntilIdle()
        assertTrue(viewModel.state.value.authenticated)
        assertEquals(FakeAuthRepository.DEMO_EMAIL, viewModel.state.value.email)
    }
}
