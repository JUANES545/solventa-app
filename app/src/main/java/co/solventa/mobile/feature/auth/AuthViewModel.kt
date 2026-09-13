package co.solventa.mobile.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.solventa.mobile.data.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AuthError { REQUIRED_FIELDS, INVALID_CREDENTIALS, NETWORK, SESSION_EXPIRED, UNKNOWN }
data class AuthUiState(
    val email: String = FakeAuthRepository.DEMO_EMAIL,
    val password: String = "",
    val loading: Boolean = false,
    val authenticated: Boolean = false,
    val error: AuthError? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun setEmail(value: String) { _state.value = _state.value.copy(email = value, error = null) }
    fun setPassword(value: String) { _state.value = _state.value.copy(password = value, error = null) }
    fun enterAsTestUser() {
        _state.value = _state.value.copy(email = FakeAuthRepository.DEMO_EMAIL, password = FakeAuthRepository.DEMO_PASSWORD)
        login()
    }
    fun login() {
        val current = _state.value
        if (current.email.isBlank() || current.password.isBlank()) {
            _state.value = current.copy(error = AuthError.REQUIRED_FIELDS)
            return
        }
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            repository.login(_state.value.email.trim(), _state.value.password).fold(
                onSuccess = { _state.value = _state.value.copy(loading = false, authenticated = true) },
                onFailure = { error ->
                    val kind = when (error) {
                        is InvalidCredentialsException -> AuthError.INVALID_CREDENTIALS
                        is SimulatedNetworkException -> AuthError.NETWORK
                        is SessionExpiredException -> AuthError.SESSION_EXPIRED
                        else -> AuthError.UNKNOWN
                    }
                    _state.value = _state.value.copy(loading = false, error = kind)
                }
            )
        }
    }
    fun consumeAuthentication() { _state.value = _state.value.copy(authenticated = false) }
}
