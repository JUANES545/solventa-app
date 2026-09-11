package co.solventa.mobile.feature.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import co.solventa.mobile.BuildConfig
import co.solventa.mobile.R
import co.solventa.mobile.core.ui.SectionTitle
import co.solventa.mobile.data.ThemePreference

@Composable
fun ProfileScreen(viewModel: SettingsViewModel, onLogout: () -> Unit, onScenarios: () -> Unit) {
    val settings by viewModel.settings.collectAsState()
    var showLogout by remember { mutableStateOf(false) }
    var paymentNotifications by remember { mutableStateOf(true) }
    var claimNotifications by remember { mutableStateOf(true) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(stringResource(R.string.profile), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        ElevatedCard(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(Modifier.size(48.dp), shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                    Box(contentAlignment = Alignment.Center) { Icon(Icons.Rounded.Person, null, tint = MaterialTheme.colorScheme.primary) }
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(stringResource(R.string.customer_name), style = MaterialTheme.typography.titleLarge)
                    Text(stringResource(R.string.customer_email), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        SectionTitle(R.string.settings)
        SectionTitle(R.string.language)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("es")) }, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.spanish)) }
            OutlinedButton(onClick = { AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en")) }, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.english)) }
        }
        SectionTitle(R.string.appearance)
        ThemePreference.entries.forEach { value ->
            val selected = settings.theme == value
            Row(
                Modifier.fillMaxWidth().heightIn(min = 52.dp).selectable(selected = selected, role = Role.RadioButton, onClick = { viewModel.setTheme(value) }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected, null)
                Text(stringResource(when (value) { ThemePreference.SYSTEM -> R.string.system_theme; ThemePreference.LIGHT -> R.string.light_theme; ThemePreference.DARK -> R.string.dark_theme }), Modifier.padding(start = 8.dp))
            }
        }
        SectionTitle(R.string.notification_preferences)
        SettingSwitch(R.string.payment_confirmed, paymentNotifications) { paymentNotifications = it }
        SettingSwitch(R.string.claim_updated, claimNotifications) { claimNotifications = it }
        SectionTitle(R.string.accessibility)
        SettingSwitch(R.string.larger_text, settings.largeText, viewModel::setLargeText)
        if (BuildConfig.DEBUG) OutlinedButton(onClick = onScenarios, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Rounded.BugReport, null); Spacer(Modifier.width(8.dp)); Text(stringResource(R.string.test_scenarios)) }
        TextButton(onClick = { showLogout = true }, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.logout), color = MaterialTheme.colorScheme.error) }
    }
    if (showLogout) AlertDialog(
        onDismissRequest = { showLogout = false },
        title = { Text(stringResource(R.string.logout)) },
        text = { Text(stringResource(R.string.logout_confirmation)) },
        confirmButton = { TextButton(onClick = { showLogout = false; onLogout() }) { Text(stringResource(R.string.logout)) } },
        dismissButton = { TextButton(onClick = { showLogout = false }) { Text(stringResource(R.string.cancel)) } }
    )
}

@Composable
private fun SettingSwitch(label: Int, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().heightIn(min = 56.dp).toggleable(checked, role = Role.Switch, onValueChange = onChecked),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(label), Modifier.weight(1f).padding(end = 12.dp)); Switch(checked, null)
    }
}
