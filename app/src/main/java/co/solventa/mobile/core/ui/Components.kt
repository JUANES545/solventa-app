package co.solventa.mobile.core.ui

import android.app.DatePickerDialog
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.solventa.mobile.R
import co.solventa.mobile.core.designsystem.SolventaLayout
import co.solventa.mobile.core.designsystem.SolventaStatusColors
import co.solventa.mobile.domain.*
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.FormatStyle
import java.time.ZoneId
import java.util.Currency
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolventaScreen(
    @StringRes title: Int,
    onBack: (() -> Unit)? = null,
    snackbarHostState: SnackbarHostState? = null,
    onHome: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        snackbarHost = { snackbarHostState?.let { SnackbarHost(it) } },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(title), fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    if (onBack != null) IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, stringResource(R.string.back))
                    }
                },
                actions = {
                    if (onHome != null) IconButton(onClick = onHome) {
                        Icon(Icons.Rounded.Home, stringResource(R.string.back_to_home))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(
                horizontal = SolventaLayout.screenHorizontalPadding,
                vertical = SolventaLayout.screenVerticalPadding
            ),
            verticalArrangement = Arrangement.spacedBy(SolventaLayout.sectionGap),
            content = content
        )
    }
}

@Composable
fun DatePickerField(
    @StringRes label: Int,
    value: String,
    onDateSelected: (String) -> Unit,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    initialDate: LocalDate = LocalDate.now()
) {
    val context = LocalContext.current
    val selectedDate = value.toLocalDateOrNull()
    val pickerDate = (selectedDate ?: initialDate).coerceWithin(minDate, maxDate)
    val openPicker = {
        DatePickerDialog(
            context,
            { _, year, month, day -> onDateSelected(LocalDate.of(year, month + 1, day).toString()) },
            pickerDate.year,
            pickerDate.monthValue - 1,
            pickerDate.dayOfMonth
        ).apply {
            minDate?.let { datePicker.minDate = it.toPickerMillis() }
            maxDate?.let { datePicker.maxDate = it.toPickerMillis() }
        }.show()
    }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            stringResource(label),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        OutlinedCard(onClick = openPicker, modifier = Modifier.fillMaxWidth()) {
            Row(
                Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    selectedDate?.let { formatDate(it.toString()) } ?: stringResource(R.string.select_date),
                    modifier = Modifier.weight(1f),
                    color = if (selectedDate == null) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
                Icon(Icons.Rounded.CalendarMonth, null, tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun PrimaryButton(@StringRes label: Int, onClick: () -> Unit, enabled: Boolean = true) {
    Button(onClick = onClick, enabled = enabled, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
        Text(stringResource(label))
    }
}

@Composable
fun ConversionButton(@StringRes label: Int, onClick: () -> Unit, enabled: Boolean = true) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = SolventaStatusColors.conversion,
            contentColor = SolventaStatusColors.onConversion
        ),
        modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
    ) {
        Text(stringResource(label))
    }
}

@Composable
fun SimulationNotice() {
    Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium) {
        Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Info, null)
            Text(stringResource(R.string.simulation_notice), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun SectionTitle(@StringRes title: Int) = Text(stringResource(title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

@Composable
fun ErrorMessage(@StringRes text: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
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
            Text(stringResource(text), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun LoadingState() = Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        CircularProgressIndicator()
        Text(stringResource(R.string.loading))
    }
}

@Composable
fun EmptyState(@StringRes message: Int) = Text(stringResource(message), modifier = Modifier.fillMaxWidth().padding(24.dp), textAlign = TextAlign.Center)

@Composable
fun LoadError(kind: ErrorKind, retry: () -> Unit) {
    Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            stringResource(if (kind == ErrorKind.SESSION_EXPIRED) R.string.session_expired else R.string.generic_load_error),
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        OutlinedButton(onClick = retry) { Text(stringResource(R.string.retry)) }
    }
}

@Composable
fun productLabel(product: InsuranceProduct) = stringResource(when (product) {
    InsuranceProduct.TRAVEL -> R.string.international_travel
    InsuranceProduct.LIFE -> R.string.life_protection
    InsuranceProduct.DEVICE -> R.string.protected_device
    InsuranceProduct.PARAMETRIC -> R.string.climate_protection
})

@Composable
fun productShortLabel(product: InsuranceProduct) = stringResource(when (product) {
    InsuranceProduct.TRAVEL -> R.string.travel
    InsuranceProduct.LIFE -> R.string.life
    InsuranceProduct.DEVICE -> R.string.device
    InsuranceProduct.PARAMETRIC -> R.string.parametric
})

@Composable
fun policyStatusLabel(status: PolicyStatus) = stringResource(when (status) {
    PolicyStatus.ACTIVE -> R.string.active
    PolicyStatus.EXPIRING -> R.string.expiring
    PolicyStatus.EXPIRED -> R.string.expired
})

@Composable
fun claimStatusLabel(status: ClaimStatus) = stringResource(when (status) {
    ClaimStatus.SUBMITTED -> R.string.submitted
    ClaimStatus.IN_REVIEW -> R.string.in_review
    ClaimStatus.APPROVED -> R.string.approved
    ClaimStatus.CLOSED -> R.string.closed
})

@Composable
fun claimTypeLabel(type: ClaimType) = stringResource(when (type) {
    ClaimType.DELAYED_FLIGHT -> R.string.delayed_flight
    ClaimType.LOST_BAGGAGE -> R.string.lost_baggage
    ClaimType.MEDICAL_ASSISTANCE -> R.string.medical_assistance
})

@Composable
fun planLabel(level: PlanLevel) = stringResource(when (level) {
    PlanLevel.ESSENTIAL -> R.string.essential_plan
    PlanLevel.PLUS -> R.string.plus_plan
    PlanLevel.PREMIUM -> R.string.premium_plan
})

fun formatCop(value: Long): String = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
    currency = Currency.getInstance("COP")
    maximumFractionDigits = 0
}.format(value)

fun formatDate(value: String): String = runCatching {
    LocalDate.parse(value).format(java.time.format.DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault()))
}.getOrDefault(value)

fun String.toLocalDateOrNull(): LocalDate? = runCatching { LocalDate.parse(this) }.getOrNull()

private fun LocalDate.coerceWithin(minDate: LocalDate?, maxDate: LocalDate?): LocalDate {
    if (minDate != null && isBefore(minDate)) return minDate
    if (maxDate != null && isAfter(maxDate)) return maxDate
    return this
}

private fun LocalDate.toPickerMillis(): Long = atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
