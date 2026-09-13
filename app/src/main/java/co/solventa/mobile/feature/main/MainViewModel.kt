package co.solventa.mobile.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.solventa.mobile.data.*
import co.solventa.mobile.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val policies: LoadState<List<Policy>> = LoadState.Loading,
    val claims: LoadState<List<Claim>> = LoadState.Loading,
    val notifications: LoadState<List<SolventaNotification>> = LoadState.Loading
)

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: InsuranceRepository, private val scenarios: TestScenarioController) : ViewModel() {
    private val _state = MutableStateFlow(MainUiState())
    val state: StateFlow<MainUiState> = _state.asStateFlow()
    init { viewModelScope.launch { scenarios.scenario.collect { load() } } }

    fun refresh() { viewModelScope.launch { load() } }

    private suspend fun load() = coroutineScope {
        _state.value = MainUiState()
        val policies = async { runCatching { repository.policies() } }
        val claims = async { runCatching { repository.claims() } }
        val notifications = async { runCatching { repository.notifications() } }
        _state.value = MainUiState(
            policies = policies.await().toLoadState(),
            claims = claims.await().toLoadState(),
            notifications = notifications.await().toLoadState()
        )
    }

    fun markNotificationRead(id: String) {
        val current = (_state.value.notifications as? LoadState.Content)?.data ?: return
        _state.value = _state.value.copy(notifications = LoadState.Content(current.map { if (it.id == id) it.copy(read = true) else it }))
    }
}

private fun <T> Result<List<T>>.toLoadState(): LoadState<List<T>> = fold(
    onSuccess = { if (it.isEmpty()) LoadState.Empty else LoadState.Content(it) },
    onFailure = {
        LoadState.Error(when (it) {
            is SimulatedNetworkException -> ErrorKind.NETWORK
            is SessionExpiredException -> ErrorKind.SESSION_EXPIRED
            else -> ErrorKind.UNKNOWN
        })
    }
)
