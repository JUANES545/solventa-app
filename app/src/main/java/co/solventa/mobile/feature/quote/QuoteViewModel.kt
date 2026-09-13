package co.solventa.mobile.feature.quote

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

data class QuoteUiState(
    val product: InsuranceProduct = InsuranceProduct.TRAVEL,
    val details: TravelDetails = TravelDetails(),
    val plans: LoadState<List<TravelPlan>> = LoadState.Idle,
    val selectedPlan: TravelPlan? = null,
    val consent: Boolean = false,
    val paymentSelected: Boolean = false,
    val otp: String = "",
    val issuing: Boolean = false,
    val issuedPolicy: Policy? = null,
    val error: ErrorKind? = null
)

@HiltViewModel
class QuoteViewModel @Inject constructor(private val repository: InsuranceRepository) : ViewModel() {
    private val _state = MutableStateFlow(QuoteUiState())
    val state: StateFlow<QuoteUiState> = _state.asStateFlow()

    fun reset() { _state.value = QuoteUiState() }

    fun setProduct(value: InsuranceProduct) { _state.value = _state.value.copy(product = value) }
    fun setDetails(destination: String, departure: String, returnDate: String, travelers: Int) {
        _state.value = _state.value.copy(details = TravelDetails(destination, departure, returnDate, travelers))
    }
    fun loadPlans() = viewModelScope.launch {
        _state.value = _state.value.copy(plans = LoadState.Loading)
        runCatching { repository.travelPlans(_state.value.details) }.fold(
            { _state.value = _state.value.copy(plans = LoadState.Content(it)) },
            { _state.value = _state.value.copy(plans = LoadState.Error(if (it is SessionExpiredException) ErrorKind.SESSION_EXPIRED else ErrorKind.NETWORK)) }
        )
    }
    fun selectPlan(value: TravelPlan) { _state.value = _state.value.copy(selectedPlan = value) }
    fun setConsent(value: Boolean) { _state.value = _state.value.copy(consent = value) }
    fun setPaymentSelected(value: Boolean) { _state.value = _state.value.copy(paymentSelected = value) }
    fun setOtp(value: String) { _state.value = _state.value.copy(otp = value.filter(Char::isDigit).take(6), error = null) }
    fun issue() {
        val plan = _state.value.selectedPlan ?: return
        if (_state.value.otp != "123456") { _state.value = _state.value.copy(error = ErrorKind.SUBMISSION); return }
        viewModelScope.launch {
            _state.value = _state.value.copy(issuing = true, error = null)
            runCatching { repository.issueTravelPolicy(plan) }.fold(
                { _state.value = _state.value.copy(issuing = false, issuedPolicy = it) },
                { _state.value = _state.value.copy(issuing = false, error = ErrorKind.NETWORK) }
            )
        }
    }
}
