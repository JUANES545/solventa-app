package co.solventa.mobile.feature.debug

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.solventa.mobile.R
import co.solventa.mobile.core.ui.SolventaScreen
import co.solventa.mobile.data.TestScenario
import co.solventa.mobile.data.TestScenarioController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScenarioViewModel @Inject constructor(private val controller: TestScenarioController) : androidx.lifecycle.ViewModel() {
    val scenario = controller.scenario
    fun select(value: TestScenario) = controller.select(value)
}

@Composable
fun ScenarioScreen(onBack: () -> Unit, viewModel: ScenarioViewModel = hiltViewModel()) {
    val selected by viewModel.scenario.collectAsState()
    SolventaScreen(R.string.test_scenarios, onBack) {
        Text(stringResource(R.string.debug_only), color = MaterialTheme.colorScheme.onSurfaceVariant)
        TestScenario.entries.forEach { value ->
            ElevatedCard(onClick = { viewModel.select(value) }, modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth().padding(16.dp)) {
                    RadioButton(selected == value, { viewModel.select(value) })
                    Column { Text(stringResource(scenarioLabel(value))); if (selected == value) Text(stringResource(R.string.scenario_selected), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary) }
                }
            }
        }
    }
}

private fun scenarioLabel(value: TestScenario) = when (value) {
    TestScenario.NORMAL -> R.string.normal_data
    TestScenario.EMPTY_LISTS -> R.string.empty_lists
    TestScenario.NETWORK_ERROR -> R.string.simulated_network_error
    TestScenario.SLOW_RESPONSE -> R.string.slow_response
    TestScenario.SESSION_EXPIRED -> R.string.expired_session
    TestScenario.CLAIM_SUCCESS -> R.string.successful_claim
    TestScenario.CLAIM_ERROR -> R.string.failed_claim
}
