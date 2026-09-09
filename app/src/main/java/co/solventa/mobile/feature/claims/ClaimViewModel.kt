package co.solventa.mobile.feature.claims

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.solventa.mobile.data.*
import co.solventa.mobile.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClaimUiState(val draft: ClaimDraft = ClaimDraft(), val submission: LoadState<Claim> = LoadState.Idle)

@HiltViewModel
class ClaimViewModel @Inject constructor(private val repository: InsuranceRepository) : ViewModel() {
    private val _state = MutableStateFlow(ClaimUiState())
    val state: StateFlow<ClaimUiState> = _state.asStateFlow()
    fun reset() { _state.value = ClaimUiState() }
    fun setEvent(date: String, description: String, type: ClaimType) { _state.value = _state.value.copy(draft = _state.value.draft.copy(eventDate = date, description = description, type = type)) }
    fun addEvidence(values: List<Uri>) { _state.value = _state.value.copy(draft = _state.value.draft.copy(evidence = (_state.value.draft.evidence + values).distinct().take(5))) }
    fun removeEvidence(value: Uri) { _state.value = _state.value.copy(draft = _state.value.draft.copy(evidence = _state.value.draft.evidence - value)) }
    fun setLocation(value: String) { _state.value = _state.value.copy(draft = _state.value.draft.copy(location = value)) }
    fun submit() = viewModelScope.launch {
        _state.value = _state.value.copy(submission = LoadState.Loading)
        runCatching { repository.submitClaim(_state.value.draft) }.fold(
            { _state.value = _state.value.copy(submission = LoadState.Content(it)) },
            { _state.value = _state.value.copy(submission = LoadState.Error(if (it is ClaimSubmissionException) ErrorKind.SUBMISSION else ErrorKind.NETWORK)) }
        )
    }
}
