package co.solventa.mobile.feature.claims

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import co.solventa.mobile.R
import co.solventa.mobile.core.ui.*
import co.solventa.mobile.domain.*
import java.time.LocalDate

@Composable
fun ClaimPolicyScreen(onBack: () -> Unit, onContinue: () -> Unit) = SolventaScreen(R.string.select_policy, onBack) {
    ElevatedCard(onClick = onContinue, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(stringResource(R.string.international_travel), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("SOL-TRV-2026-1842"); Text(stringResource(R.string.active), color = MaterialTheme.colorScheme.secondary)
        }
    }
    PrimaryButton(R.string.continue_action, onContinue)
}

@Composable
fun ClaimEventScreen(viewModel: ClaimViewModel, onBack: () -> Unit, onContinue: () -> Unit) {
    val state by viewModel.state.collectAsState()
    var date by rememberSaveable { mutableStateOf(state.draft.eventDate) }
    var description by rememberSaveable { mutableStateOf(state.draft.description) }
    var type by rememberSaveable { mutableStateOf(state.draft.type) }
    var error by rememberSaveable { mutableStateOf(false) }
    SolventaScreen(R.string.event_information, onBack) {
        Text(stringResource(R.string.claim_type), fontWeight = FontWeight.SemiBold)
        ClaimType.entries.forEach { value -> FilterChip(selected = type == value, onClick = { type = value }, label = { Text(claimTypeLabel(value)) }) }
        DatePickerField(
            label = R.string.event_date,
            value = date,
            onDateSelected = { date = it; error = false },
            maxDate = LocalDate.now()
        )
        OutlinedTextField(description, { description = it; error = false }, label = { Text(stringResource(R.string.description)) }, modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp), minLines = 3)
        if (error) ErrorMessage(R.string.event_fields_error)
        PrimaryButton(R.string.continue_action, {
            if (date.isBlank() || description.isBlank()) error = true else { viewModel.setEvent(date, description, type); onContinue() }
        })
    }
}

@Composable
fun EvidenceScreen(viewModel: ClaimViewModel, onBack: () -> Unit, onContinue: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { values -> viewModel.addEvidence(values) }
    SolventaScreen(R.string.evidence_title, onBack) {
        Text(stringResource(R.string.evidence_help))
        OutlinedButton(
            onClick = { picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
        ) { Text(stringResource(R.string.select_photos)) }
        if (state.draft.evidence.isNotEmpty()) {
            Text(stringResource(R.string.photo_preview), fontWeight = FontWeight.SemiBold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.draft.evidence, key = Uri::toString) { uri ->
                    ElevatedCard { Column(Modifier.width(160.dp).padding(8.dp)) { AsyncImage(uri, stringResource(R.string.photo_preview), Modifier.fillMaxWidth().height(110.dp), contentScale = ContentScale.Crop); TextButton(onClick = { viewModel.removeEvidence(uri) }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.remove)) } } }
                }
            }
        }
        PrimaryButton(R.string.continue_action, onContinue)
    }
}

@Composable
fun LocationScreen(viewModel: ClaimViewModel, onBack: () -> Unit, onContinue: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var denied by rememberSaveable { mutableStateOf(false) }
    var unavailable by rememberSaveable { mutableStateOf(false) }
    val coordinatesTemplate = stringResource(R.string.location_coordinates)
    fun retrieve() = getLastLocation(context) { location ->
        if (location == null) unavailable = true
        else { unavailable = false; viewModel.setLocation(String.format(coordinatesTemplate, location.latitude, location.longitude)) }
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted -> if (granted) retrieve() else denied = true }
    SolventaScreen(R.string.location_title, onBack) {
        OutlinedButton(onClick = {
            denied = false
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) retrieve()
            else permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) { Text(stringResource(R.string.use_device_location)) }
        if (denied) ErrorMessage(R.string.location_permission_denied)
        if (unavailable) ErrorMessage(R.string.location_unavailable)
        OutlinedTextField(state.draft.location, viewModel::setLocation, label = { Text(stringResource(R.string.manual_location)) }, modifier = Modifier.fillMaxWidth())
        OutlinedButton(onClick = { viewModel.setLocation(context.getString(R.string.demo_location_value)); denied = false; unavailable = false }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.demo_location)) }
        PrimaryButton(R.string.continue_action, onContinue, state.draft.location.isNotBlank())
    }
}

@Composable
fun ReviewClaimScreen(viewModel: ClaimViewModel, onBack: () -> Unit, onSubmitted: () -> Unit) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state.submission) { if (state.submission is LoadState.Content) onSubmitted() }
    SolventaScreen(R.string.review_claim, onBack) {
        Text(stringResource(R.string.international_travel), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(claimTypeLabel(state.draft.type)); Text(formatDate(state.draft.eventDate)); Text(state.draft.description)
        HorizontalDivider(); SectionTitle(R.string.evidence_title); Text(state.draft.evidence.size.toString())
        SectionTitle(R.string.location_title); Text(state.draft.location)
        SimulationNotice()
        when (state.submission) {
            LoadState.Loading -> LoadingState()
            is LoadState.Error -> { ErrorMessage(R.string.claim_send_error); PrimaryButton(R.string.retry, viewModel::submit) }
            else -> PrimaryButton(R.string.submit_claim, viewModel::submit)
        }
    }
}

@Composable
fun ClaimResultScreen(viewModel: ClaimViewModel, onClaims: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val claim = (state.submission as? LoadState.Content)?.data
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(Modifier.fillMaxSize().systemBarsPadding().padding(28.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Rounded.CheckCircle, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(72.dp)); Spacer(Modifier.height(20.dp))
            Text(stringResource(R.string.claim_sent), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            claim?.let { Text(stringResource(R.string.claim_reference, it.id), textAlign = TextAlign.Center) }
            Spacer(Modifier.height(28.dp)); PrimaryButton(R.string.back_to_claims, onClaims); Spacer(Modifier.height(16.dp)); SimulationNotice()
        }
    }
}

@SuppressLint("MissingPermission")
private fun getLastLocation(context: Context, result: (Location?) -> Unit) {
    val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val location = manager.getProviders(true).mapNotNull { provider -> runCatching { manager.getLastKnownLocation(provider) }.getOrNull() }.maxByOrNull(Location::getTime)
    result(location)
}
