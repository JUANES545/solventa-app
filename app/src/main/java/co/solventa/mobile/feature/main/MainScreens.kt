package co.solventa.mobile.feature.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.solventa.mobile.R
import co.solventa.mobile.core.ui.*
import co.solventa.mobile.domain.*
import kotlinx.coroutines.launch

data class BottomDestination(val route: String, val label: Int, val icon: androidx.compose.ui.graphics.vector.ImageVector)

val bottomDestinations = listOf(
    BottomDestination("home", R.string.home, Icons.Rounded.Home),
    BottomDestination("policies", R.string.policies, Icons.Rounded.Shield),
    BottomDestination("claims", R.string.claims, Icons.Rounded.ReportProblem),
    BottomDestination("notifications", R.string.notifications, Icons.Rounded.Notifications),
    BottomDestination("profile", R.string.profile, Icons.Rounded.Person)
)

@Composable
fun MainScaffold(currentRoute: String, navigate: (String) -> Unit, content: @Composable (PaddingValues) -> Unit) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomDestinations.forEach { destination ->
                    NavigationBarItem(
                        selected = currentRoute == destination.route,
                        onClick = { navigate(destination.route) },
                        icon = { Icon(destination.icon, null) },
                        label = { Text(stringResource(destination.label)) },
                        alwaysShowLabel = false
                    )
                }
            }
        },
        content = content
    )
}

@Composable
fun HomeScreen(state: MainUiState, onQuote: () -> Unit, onClaim: () -> Unit, onPolicy: (String) -> Unit, onRetry: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text(stringResource(R.string.hello_user), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(stringResource(R.string.home_subtitle), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onQuote, modifier = Modifier.weight(1f).heightIn(min = 64.dp)) { Icon(Icons.Rounded.FlightTakeoff, null); Spacer(Modifier.width(8.dp)); Text(stringResource(R.string.new_quote)) }
                OutlinedButton(onClick = onClaim, modifier = Modifier.weight(1f).heightIn(min = 64.dp)) { Icon(Icons.Rounded.AddAlert, null); Spacer(Modifier.width(8.dp)); Text(stringResource(R.string.report_claim)) }
            }
        }
        item { SectionTitle(R.string.active_policies) }
        when (val policies = state.policies) {
            LoadState.Loading -> item { LoadingState() }
            LoadState.Empty -> item { EmptyState(R.string.empty_policies) }
            is LoadState.Error -> item { LoadError(policies.kind, onRetry) }
            is LoadState.Content -> items(policies.data.filter { it.status == PolicyStatus.ACTIVE }.take(2), key = { it.id }) { PolicyCard(it) { onPolicy(it.id) } }
            else -> Unit
        }
        item { SectionTitle(R.string.upcoming_trip) }
        item { ElevatedCard(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) { Text(stringResource(R.string.international_travel), fontWeight = FontWeight.SemiBold); Text("Bogotá → Madrid · ${formatDate("2026-10-04")}", color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
        item { SectionTitle(R.string.recent_notifications) }
        when (val notifications = state.notifications) {
            is LoadState.Content -> items(notifications.data.take(2), key = { it.id }) { NotificationRow(it, null) }
            LoadState.Loading -> item { LoadingState() }
            LoadState.Empty -> item { EmptyState(R.string.empty_notifications) }
            is LoadState.Error -> item { LoadError(notifications.kind, onRetry) }
            else -> Unit
        }
    }
}

@Composable
fun PoliciesScreen(state: LoadState<List<Policy>>, onPolicy: (String) -> Unit, onRetry: () -> Unit) {
    var filter by remember { mutableStateOf<PolicyStatus?>(null) }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text(stringResource(R.string.policies), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold) }
        item {
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(null to R.string.filter_all, PolicyStatus.ACTIVE to R.string.filter_active, PolicyStatus.EXPIRING to R.string.filter_expiring, PolicyStatus.EXPIRED to R.string.filter_expired).forEach { (value, label) ->
                    FilterChip(selected = filter == value, onClick = { filter = value }, label = { Text(stringResource(label)) })
                }
            }
        }
        when (state) {
            LoadState.Loading -> item { LoadingState() }
            LoadState.Empty -> item { EmptyState(R.string.empty_policies) }
            is LoadState.Error -> item { LoadError(state.kind, onRetry) }
            is LoadState.Content -> {
                val filtered = state.data.filter { filter == null || it.status == filter }
                if (filtered.isEmpty()) item { EmptyState(R.string.empty_policies) }
                else items(filtered, key = { it.id }) { PolicyCard(it) { onPolicy(it.id) } }
            }
            else -> Unit
        }
    }
}

@Composable
fun PolicyCard(policy: Policy, onClick: () -> Unit) {
    ElevatedCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(productName(policy.product), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(policyStatusName(policy.status), style = MaterialTheme.typography.labelMedium, color = if (policy.status == PolicyStatus.EXPIRED) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary)
            }
            Text(policy.id, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(formatCop(policy.premiumCop), style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
fun PolicyDetailScreen(policy: Policy, onBack: () -> Unit, onNewClaim: () -> Unit) {
    val snackbar = remember { SnackbarHostState() }
    val actionMessage = stringResource(R.string.action_completed)
    val scope = rememberCoroutineScope()
    SolventaScreen(R.string.policy_detail, onBack, snackbar) {
        Text(productName(policy.product), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(policy.id, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(policyStatusName(policy.status), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
        SectionTitle(R.string.premium); Text(formatCop(policy.premiumCop), style = MaterialTheme.typography.headlineSmall)
        SectionTitle(R.string.validity); Text("${formatDate(policy.validFrom)} — ${formatDate(policy.validUntil)}")
        SectionTitle(R.string.coverage)
        policy.coverage.forEach { Text("• ${coverageName(it)}") }
        if (policy.product == InsuranceProduct.TRAVEL) PrimaryButton(R.string.report_claim, onNewClaim)
        OutlinedButton(onClick = { scope.launch { snackbar.showSnackbar(actionMessage) } }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.download_policy)) }
        OutlinedButton(onClick = { scope.launch { snackbar.showSnackbar(actionMessage) } }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.renew_policy)) }
        TextButton(onClick = { scope.launch { snackbar.showSnackbar(actionMessage) } }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.cancel_policy), color = MaterialTheme.colorScheme.error) }
        SimulationNotice()
    }
}

@Composable
fun ClaimsScreen(state: LoadState<List<Claim>>, onNew: () -> Unit, onClaim: (String) -> Unit, onRetry: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(stringResource(R.string.claims), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold); FilledIconButton(onClick = onNew) { Icon(Icons.Rounded.Add, stringResource(R.string.new_claim)) } } }
        when (state) {
            LoadState.Loading -> item { LoadingState() }
            LoadState.Empty -> item { EmptyState(R.string.empty_claims); PrimaryButton(R.string.new_claim, onNew) }
            is LoadState.Error -> item { LoadError(state.kind, onRetry) }
            is LoadState.Content -> items(state.data, key = { it.id }) { claim ->
                ElevatedCard(onClick = { onClaim(claim.id) }, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(claimTypeName(claim.type), fontWeight = FontWeight.SemiBold)
                        Text(claim.id, style = MaterialTheme.typography.bodySmall)
                        Text(claimStatusName(claim.status), color = MaterialTheme.colorScheme.secondary)
                        Text(formatDate(claim.eventDate), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            else -> Unit
        }
    }
}

@Composable
fun ClaimDetailScreen(claim: Claim, onBack: () -> Unit) = SolventaScreen(R.string.claim_detail, onBack) {
    Text(claimTypeName(claim.type), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    Text(claim.id)
    Text(claimStatusName(claim.status), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
    Text(formatDate(claim.eventDate)); Text(if (claim.type == ClaimType.DELAYED_FLIGHT) stringResource(R.string.delayed_flight_description) else stringResource(R.string.lost_baggage_description))
    HorizontalDivider(); Text(stringResource(R.string.international_travel), fontWeight = FontWeight.SemiBold); Text(claim.policyId)
    SimulationNotice()
}

@Composable
fun NotificationsScreen(state: LoadState<List<SolventaNotification>>, onRead: (String) -> Unit, onRetry: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text(stringResource(R.string.notifications), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold) }
        when (state) {
            LoadState.Loading -> item { LoadingState() }
            LoadState.Empty -> item { EmptyState(R.string.empty_notifications) }
            is LoadState.Error -> item { LoadError(state.kind, onRetry) }
            is LoadState.Content -> items(state.data, key = { it.id }) { NotificationRow(it) { onRead(it.id) } }
            else -> Unit
        }
    }
}

@Composable
private fun NotificationRow(notification: SolventaNotification, onRead: (() -> Unit)?) {
    if (onRead == null) ElevatedCard(modifier = Modifier.fillMaxWidth()) { NotificationContent(notification) }
    else ElevatedCard(onClick = onRead, modifier = Modifier.fillMaxWidth()) { NotificationContent(notification) }
}

@Composable
private fun NotificationContent(notification: SolventaNotification) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (!notification.read) Badge()
            Column(Modifier.weight(1f)) { Text(notificationName(notification.type), fontWeight = if (notification.read) FontWeight.Normal else FontWeight.Bold); Text(formatDate(notification.createdAt), style = MaterialTheme.typography.bodySmall) }
            if (!notification.read) Text(stringResource(R.string.mark_read), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        }
}

@Composable private fun productName(value: InsuranceProduct) = productLabel(value)
@Composable private fun policyStatusName(value: PolicyStatus) = policyStatusLabel(value)
@Composable private fun claimStatusName(value: ClaimStatus) = claimStatusLabel(value)
@Composable private fun claimTypeName(value: ClaimType) = claimTypeLabel(value)
@Composable private fun coverageName(value: String) = when (value) {
    "medical" -> stringResource(R.string.coverage_medical); "baggage" -> stringResource(R.string.coverage_baggage); "delay" -> stringResource(R.string.coverage_delay); else -> value
}
@Composable private fun notificationName(value: NotificationType) = when (value) {
    NotificationType.PAYMENT -> stringResource(R.string.payment_confirmed); NotificationType.EXPIRATION -> stringResource(R.string.policy_expiration_notice); NotificationType.CLAIM_UPDATE -> stringResource(R.string.claim_updated); NotificationType.ADJUSTER -> stringResource(R.string.adjuster_assigned)
}
