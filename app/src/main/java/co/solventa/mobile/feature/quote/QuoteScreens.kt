package co.solventa.mobile.feature.quote

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.solventa.mobile.R
import co.solventa.mobile.core.ui.*
import co.solventa.mobile.domain.*

@Composable
fun ProductScreen(viewModel: QuoteViewModel, onBack: () -> Unit, onTravel: () -> Unit) {
    val state by viewModel.state.collectAsState()
    SolventaScreen(R.string.quote_title, onBack) {
        SectionTitle(R.string.choose_product)
        InsuranceProduct.entries.forEach { product ->
            val selected = state.product == product
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth().selectable(selected = selected, role = Role.RadioButton, onClick = { viewModel.setProduct(product) })
            ) {
                Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(productLabel(product), Modifier.weight(1f).padding(end = 12.dp), fontWeight = FontWeight.SemiBold)
                    if (selected) Icon(Icons.Rounded.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
        Text(stringResource(if (state.product == InsuranceProduct.TRAVEL) R.string.travel_complete_flow else R.string.short_flow_notice), color = MaterialTheme.colorScheme.onSurfaceVariant)
        PrimaryButton(R.string.continue_action, onTravel, state.product == InsuranceProduct.TRAVEL)
    }
}

@Composable
fun TravelDetailsScreen(viewModel: QuoteViewModel, onBack: () -> Unit, onPlans: () -> Unit) {
    val state by viewModel.state.collectAsState()
    var destination by rememberSaveable { mutableStateOf(state.details.destination) }
    var departure by rememberSaveable { mutableStateOf(state.details.departureDate) }
    var returnDate by rememberSaveable { mutableStateOf(state.details.returnDate) }
    var travelers by rememberSaveable { mutableIntStateOf(state.details.travelers) }
    var showError by rememberSaveable { mutableStateOf(false) }
    SolventaScreen(R.string.travel_details, onBack) {
        OutlinedTextField(destination, { destination = it; showError = false }, label = { Text(stringResource(R.string.destination)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(departure, { departure = it; showError = false }, label = { Text(stringResource(R.string.departure_date)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(returnDate, { returnDate = it; showError = false }, label = { Text(stringResource(R.string.return_date)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Text(stringResource(R.string.travelers), fontWeight = FontWeight.SemiBold)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(onClick = { if (travelers > 1) travelers-- }) { Text("−") }
            Text(travelers.toString(), style = MaterialTheme.typography.titleLarge)
            OutlinedButton(onClick = { if (travelers < 8) travelers++ }) { Text("+") }
        }
        if (showError) ErrorMessage(R.string.travel_fields_error)
        PrimaryButton(R.string.calculate_plans, {
            if (destination.isBlank() || departure.isBlank() || returnDate.isBlank()) showError = true
            else { viewModel.setDetails(destination, departure, returnDate, travelers); viewModel.loadPlans(); onPlans() }
        })
    }
}

@Composable
fun PlansScreen(viewModel: QuoteViewModel, onBack: () -> Unit, onContinue: () -> Unit) {
    val state by viewModel.state.collectAsState()
    SolventaScreen(R.string.plans_title, onBack) {
        when (val plans = state.plans) {
            LoadState.Loading -> LoadingState()
            is LoadState.Error -> LoadError(plans.kind, viewModel::loadPlans)
            is LoadState.Content -> plans.data.forEach { plan ->
                val selected = state.selectedPlan?.id == plan.id
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth().selectable(selected = selected, role = Role.RadioButton, onClick = { viewModel.selectPlan(plan) })
                ) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(planLabel(plan.level), Modifier.weight(1f).padding(end = 12.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text(formatCop(plan.priceCop), style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.End)
                        }
                        Text(stringResource(R.string.medical_coverage, formatCop(plan.medicalCoverageCop)))
                        Text(stringResource(R.string.baggage_coverage, formatCop(plan.baggageCoverageCop)))
                    }
                }
            }
            else -> Unit
        }
        PrimaryButton(R.string.continue_action, onContinue, state.selectedPlan != null)
    }
}

@Composable
fun QuoteConsentScreen(viewModel: QuoteViewModel, onBack: () -> Unit, onContinue: () -> Unit) {
    val state by viewModel.state.collectAsState()
    var attempted by rememberSaveable { mutableStateOf(false) }
    SolventaScreen(R.string.quote_consent_title, onBack) {
        Text(stringResource(R.string.quote_consent_body))
        Row(
            Modifier.fillMaxWidth().toggleable(state.consent, role = Role.Checkbox, onValueChange = viewModel::setConsent).padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(state.consent, null)
            Text(stringResource(R.string.open_finance_consent), Modifier.padding(start = 8.dp))
        }
        if (attempted && !state.consent) ErrorMessage(R.string.consent_required)
        PrimaryButton(R.string.continue_action, { attempted = true; if (state.consent) onContinue() })
    }
}

@Composable
fun PaymentScreen(viewModel: QuoteViewModel, onBack: () -> Unit, onContinue: () -> Unit) {
    val state by viewModel.state.collectAsState()
    SolventaScreen(R.string.payment_title, onBack) {
        SimulationNotice()
        state.selectedPlan?.let { Text("${planLabel(it.level)} · ${formatCop(it.priceCop)}", style = MaterialTheme.typography.headlineSmall) }
        ElevatedCard(modifier = Modifier.fillMaxWidth().selectable(selected = state.paymentSelected, role = Role.RadioButton, onClick = { viewModel.setPaymentSelected(true) })) {
            Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) { RadioButton(state.paymentSelected, null); Text(stringResource(R.string.payment_method), Modifier.padding(start = 8.dp)) }
        }
        Text(stringResource(R.string.payment_disclaimer), color = MaterialTheme.colorScheme.onSurfaceVariant)
        PrimaryButton(R.string.continue_action, onContinue, state.paymentSelected)
    }
}

@Composable
fun OtpScreen(viewModel: QuoteViewModel, onBack: () -> Unit, onIssued: () -> Unit) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state.issuedPolicy) { if (state.issuedPolicy != null) onIssued() }
    SolventaScreen(R.string.otp_title, onBack) {
        SimulationNotice(); Text(stringResource(R.string.otp_help))
        OutlinedTextField(
            state.otp,
            { value -> viewModel.setOtp(value.filter(Char::isDigit).take(6)) },
            label = { Text(stringResource(R.string.otp_code)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        if (state.error != null) ErrorMessage(if (state.error == ErrorKind.SUBMISSION) R.string.invalid_otp else R.string.network_error)
        if (state.issuing) LoadingState() else PrimaryButton(R.string.issue_policy, viewModel::issue, state.otp.length == 6)
    }
}

@Composable
fun IssuedScreen(onPolicy: () -> Unit) {
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(Modifier.fillMaxSize().systemBarsPadding().padding(28.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Rounded.CheckCircle, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(72.dp))
            Spacer(Modifier.height(20.dp)); Text(stringResource(R.string.policy_issued), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(stringResource(R.string.issued_reference), textAlign = TextAlign.Center); Spacer(Modifier.height(28.dp)); PrimaryButton(R.string.view_policy, onPolicy); Spacer(Modifier.height(16.dp)); SimulationNotice()
        }
    }
}
