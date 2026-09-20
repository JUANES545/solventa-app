package co.solventa.mobile.feature.auth

import androidx.compose.foundation.background
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Fingerprint
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.solventa.mobile.R
import co.solventa.mobile.core.ui.*

@Composable
fun WelcomeScreen(onLogin: () -> Unit, onRegister: () -> Unit) {
    val backgroundMotion = rememberInfiniteTransition(label = "welcome background")
    val drift by backgroundMotion.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 16_000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "background drift"
    )
    val density = LocalDensity.current
    val horizontalDrift = with(density) { 22.dp.toPx() }
    val verticalDrift = with(density) { 14.dp.toPx() }
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box {
            Box(
                Modifier
                    .size(280.dp)
                    .offset(x = 190.dp, y = (-70).dp)
                    .graphicsLayer {
                        translationX = drift * horizontalDrift
                        translationY = drift * verticalDrift
                        scaleX = 1f + (drift * 0.025f)
                        scaleY = scaleX
                    }
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.07f), CircleShape)
            )
            Box(
                Modifier
                    .align(Alignment.BottomStart)
                    .size(190.dp)
                    .offset(x = (-105).dp, y = 65.dp)
                    .graphicsLayer {
                        translationX = -drift * horizontalDrift
                        translationY = -drift * verticalDrift
                    }
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.045f), CircleShape)
            )
            Column(
                Modifier.fillMaxSize().systemBarsPadding().padding(horizontal = 28.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SolventaBrandMark(88.dp)
                Spacer(Modifier.height(28.dp))
                Text(
                    stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    stringResource(R.string.welcome_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(14.dp))
                Text(
                    stringResource(R.string.welcome_body),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(36.dp))
                PrimaryButton(R.string.sign_in, onLogin)
                Spacer(Modifier.height(10.dp))
                OutlinedButton(onClick = onRegister, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
                    Text(stringResource(R.string.create_account))
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onBack: () -> Unit,
    onAuthenticated: () -> Unit,
    onRecovery: () -> Unit,
    onRegister: () -> Unit,
    onScenarios: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(state.authenticated) {
        if (state.authenticated) { viewModel.consumeAuthentication(); onAuthenticated() }
    }
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { scaffoldPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, enabled = !state.loading) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, stringResource(R.string.back))
                }
                Spacer(Modifier.width(8.dp))
                SolventaBrandMark(48.dp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(stringResource(R.string.app_name), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(stringResource(R.string.secure_access), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(stringResource(R.string.welcome_back), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(stringResource(R.string.login_subtitle), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
            ) {
                Column(
                    Modifier.fillMaxWidth().padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = viewModel::setEmail,
                        label = { Text(stringResource(R.string.email)) },
                        leadingIcon = { Icon(Icons.Rounded.AlternateEmail, null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() }),
                        singleLine = true,
                        isError = state.error == AuthError.REQUIRED_FIELDS || state.error == AuthError.INVALID_CREDENTIALS,
                        enabled = !state.loading,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = viewModel::setPassword,
                        label = { Text(stringResource(R.string.password)) },
                        leadingIcon = { Icon(Icons.Rounded.Lock, null) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                    stringResource(if (passwordVisible) R.string.hide_password else R.string.show_password)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                            if (!state.loading) viewModel.login()
                        }),
                        singleLine = true,
                        isError = state.error == AuthError.REQUIRED_FIELDS || state.error == AuthError.INVALID_CREDENTIALS,
                        enabled = !state.loading,
                        modifier = Modifier.fillMaxWidth().focusRequester(passwordFocusRequester)
                    )
                    TextButton(onClick = onRecovery, enabled = !state.loading, modifier = Modifier.align(Alignment.End)) {
                        Text(stringResource(R.string.forgot_password))
                    }
                    state.error?.let { LoginError(it) }
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.login()
                        },
                        enabled = !state.loading,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp)
                    ) {
                        if (state.loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(stringResource(R.string.signing_in))
                        } else Text(stringResource(R.string.sign_in))
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(Modifier.weight(1f))
                Text(
                    stringResource(R.string.alternative_access),
                    modifier = Modifier.padding(horizontal = 12.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                HorizontalDivider(Modifier.weight(1f))
            }

            OutlinedButton(
                onClick = viewModel::enterAsTestUser,
                enabled = !state.loading,
                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
            ) { Text(stringResource(R.string.test_user_login)) }
            TextButton(onClick = onAuthenticated, enabled = !state.loading, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Icon(Icons.Rounded.Fingerprint, null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.biometric_access))
            }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    stringResource(R.string.no_account),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                TextButton(onClick = onRegister, enabled = !state.loading) { Text(stringResource(R.string.create_account)) }
            }
            if (co.solventa.mobile.BuildConfig.DEBUG) {
                TextButton(onClick = onScenarios, enabled = !state.loading, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Icon(Icons.Rounded.BugReport, null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.test_scenarios))
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun LoginError(error: AuthError) {
    val message = when (error) {
        AuthError.REQUIRED_FIELDS -> R.string.required_fields_error
        AuthError.INVALID_CREDENTIALS -> R.string.invalid_credentials_error
        AuthError.NETWORK -> R.string.network_error
        AuthError.SESSION_EXPIRED -> R.string.session_expired
        AuthError.UNKNOWN -> R.string.unknown_error
    }
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Rounded.ErrorOutline, null)
            Text(stringResource(message), style = MaterialTheme.typography.bodyMedium)
        }
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
    val defaultName = stringResource(R.string.customer_name)
    val defaultEmail = stringResource(R.string.customer_email)
    val defaultDocument = stringResource(R.string.sample_document_number)
    var name by rememberSaveable { mutableStateOf(defaultName) }
    var email by rememberSaveable { mutableStateOf(defaultEmail) }
    var document by rememberSaveable { mutableStateOf(defaultDocument) }
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
        Row(
            Modifier.fillMaxWidth().toggleable(terms, role = Role.Checkbox, onValueChange = { terms = it }).padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(terms, null)
            Text(stringResource(R.string.accept_terms), Modifier.padding(start = 8.dp))
        }
        Row(
            Modifier.fillMaxWidth().toggleable(finance, role = Role.Checkbox, onValueChange = { finance = it }).padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(finance, null)
            Text(stringResource(R.string.open_finance_consent), Modifier.padding(start = 8.dp))
        }
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
