package co.solventa.mobile.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore by preferencesDataStore("solventa_settings")

enum class ThemePreference { SYSTEM, LIGHT, DARK }
data class AppSettings(val theme: ThemePreference = ThemePreference.SYSTEM, val largeText: Boolean = false)

@Singleton
class SettingsRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private val themeKey = stringPreferencesKey("theme")
    private val largeTextKey = booleanPreferencesKey("large_text")

    val settings: Flow<AppSettings> = context.settingsDataStore.data.map { values ->
        AppSettings(
            theme = runCatching { ThemePreference.valueOf(values[themeKey].orEmpty()) }.getOrDefault(ThemePreference.SYSTEM),
            largeText = values[largeTextKey] ?: false
        )
    }

    suspend fun setTheme(theme: ThemePreference) { context.settingsDataStore.edit { it[themeKey] = theme.name } }
    suspend fun setLargeText(enabled: Boolean) { context.settingsDataStore.edit { it[largeTextKey] = enabled } }
}
