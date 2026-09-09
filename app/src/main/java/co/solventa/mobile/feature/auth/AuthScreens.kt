package co.solventa.mobile.feature.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HealthAndSafety
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.solventa.mobile.R
import co.solventa.mobile.core.ui.*

@Composable
fun WelcomeScreen(onLogin: () -> Unit, onRegister: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Rounded.HealthAndSafety, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(72.dp))
        Spacer(Modifier.height(24.dp))
        Text(stringResource(R.string.app_name), style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
        Text(stringResource(R.string.welcome_title), style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        Text(stringResource(R.string.welcome_body), style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(32.dp))
        PrimaryButton(R.string.sign_in, onLogin)
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onRegister, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) { Text(stringResource(R.string.create_account)) }
    }
}

@Composable
fun LoginScreen(viewModel: AuthViewModel, onAuthenticated: () -> Unit, onRecovery: () -> Unit, onRegister: () -> Unit, onScenarios: () -> Unit) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state.authenticated) {
        if (state.authenticated) { viewModel.consumeAuthentication(); onAuthenticated() }
    }
    SolventaScreen(R.string.sign_in) {
        Text(stringResource(R.string.demo_credentials), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::setEmail,
            label = { Text(stringResource(R.string.email)) },
            singleLine = true,
            enabled = !state.loading,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::setPassword,
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            enabled = !state.loading,
            modifier = Modifier.fillMaxWidth()
        )
        state.error?.let { ErrorMessage(when (it) {
            AuthError.REQUIRED_FIELDS -> R.string.required_fields_error
            AuthError.INVALID_CREDENTIALS -> R.string.invalid_credentials_error
            AuthError.NETWORK -> R.string.network_error
            AuthError.SESSION_EXPIRED -> R.string.session_expired
            AuthError.UNKNOWN -> R.string.unknown_error
        }) }
        if (state.loading) LoadingState() else {
            PrimaryButton(R.string.sign_in, viewModel::login)
            OutlinedButton(onClick = viewModel::enterAsTestUser, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) { Text(stringResource(R.string.test_user_login)) }
            OutlinedButton(onClick = onAuthenticated, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) { Text(stringResource(R.string.biometric_access)) }
            TextButton(onClick = onRecovery, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text(stringResource(R.string.forgot_password)) }
            TextButton(onClick = onRegister, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text(stringResource(R.string.create_account)) }
            if (co.solventa.mobile.BuildConfig.DEBUG) OutlinedButton(onClick = onScenarios, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.test_scenarios)) }
        }
        SimulationNotice()
    }
}

@Composable
fun RecoveryScreen(onBack: () -> Unit) {
    var email by rememberSaveable { mutableStateOf("") }
    var sent by rememberSaveable { mutableStateOf(false) }
    SolventaScreen(R.string.recovery_title, onBack) {
        Text(stringResource(R.string.recovery_help))
        OutlinedTextField(email, { email = it; sent = false }, label = { Text(stringResource(R.string.email)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        if (sent) Text(stringResource(R.string.recovery_sent), color = MaterialTheme.colorScheme.secondary)
        PrimaryButton(R.string.send_instructions, { sent = true }, email.isNotBlank())
        SimulationNotice()
    }
}

@Composable
fun RegistrationScreen(onBack: () -> Unit, onContinue: () -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var document by rememberSaveable { mutableStateOf("") }
    SolventaScreen(R.string.register_title, onBack) {
        OutlinedTextField(name, { name = it }, label = { Text(stringResource(R.string.full_name)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(email, { email = it }, label = { Text(stringResource(R.string.email)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(document, { document = it }, label = { Text(stringResource(R.string.document_number)) }, modifier = Modifier.fillMaxWidth())
        PrimaryButton(R.string.continue_action, onContinue, name.isNotBlank() && email.isNotBlank() && document.isNotBlank())
    }
}

@Composable
fun TermsScreen(onBack: () -> Unit, onContinue: () -> Unit) {
    var terms by rememberSaveable { mutableStateOf(false) }
    var finance by rememberSaveable { mutableStateOf(false) }
    SolventaScreen(R.string.terms_title, onBack) {
        Row(verticalAlignment = Alignment.Top) { Checkbox(terms, { terms = it }); Text(stringResource(R.string.accept_terms), Modifier.padding(top = 12.dp)) }
        Row(verticalAlignment = Alignment.Top) { Checkbox(finance, { finance = it }); Text(stringResource(R.string.open_finance_consent), Modifier.padding(top = 12.dp)) }
        PrimaryButton(R.string.continue_action, onContinue, terms)
    }
}

@Composable
fun KycScreen(onBack: () -> Unit, onFinished: () -> Unit) {
    var documentCaptured by rememberSaveable { mutableStateOf(false) }
    var selfieCaptured by rememberSaveable { mutableStateOf(false) }
    SolventaScreen(R.string.kyc_title, onBack) {
        SimulationNotice()
        OutlinedButton(onClick = { documentCaptured = true }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
            Text(if (documentCaptured) "✓ ${stringResource(R.string.capture_document)}" else stringResource(R.string.capture_document))
        }
        OutlinedButton(onClick = { selfieCaptured = true }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
            Text(if (selfieCaptured) "✓ ${stringResource(R.string.capture_selfie)}" else stringResource(R.string.capture_selfie))
        }
        if (documentCaptured && selfieCaptured) Text(stringResource(R.string.kyc_approved), color = MaterialTheme.colorScheme.secondary)
        PrimaryButton(R.string.finish_registration, onFinished, documentCaptured && selfieCaptured)
    }
}

@Preview(showBackground = true)
@Composable private fun WelcomePreview() { MaterialTheme { WelcomeScreen({}, {}) } }
