package co.solventa.mobile.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class TestScenario {
    NORMAL,
    EMPTY_LISTS,
    NETWORK_ERROR,
    SLOW_RESPONSE,
    SESSION_EXPIRED,
    CLAIM_SUCCESS,
    CLAIM_ERROR
}

@Singleton
class TestScenarioController @Inject constructor() {
    private val _scenario = MutableStateFlow(TestScenario.NORMAL)
    val scenario: StateFlow<TestScenario> = _scenario.asStateFlow()
    fun select(value: TestScenario) { _scenario.value = value }
}
