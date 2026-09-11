package co.solventa.mobile.core.designsystem

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.core.view.WindowCompat
import co.solventa.mobile.data.ThemePreference

private val LightColors = lightColorScheme(
    primary = Color(0xFF123B6D), onPrimary = Color.White,
    primaryContainer = Color(0xFFD9E8FB), onPrimaryContainer = Color(0xFF071C33),
    secondary = Color(0xFF007FA3), onSecondary = Color.White,
    secondaryContainer = Color(0xFFD5F4FF), onSecondaryContainer = Color(0xFF003544),
    tertiary = Color(0xFF047857), onTertiary = Color.White,
    background = Color(0xFFF7F9FC), onBackground = Color(0xFF0B1F3A),
    surface = Color.White, onSurface = Color(0xFF0B1F3A),
    surfaceVariant = Color(0xFFE8EEF6), onSurfaceVariant = Color(0xFF475569),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF1F5FA),
    surfaceContainer = Color(0xFFEAF0F7),
    surfaceContainerHigh = Color(0xFFE2EAF3),
    surfaceContainerHighest = Color(0xFFD9E3EE),
    outline = Color(0xFF64748B), outlineVariant = Color(0xFFCBD5E1),
    error = Color(0xFFB42318), onError = Color.White,
    errorContainer = Color(0xFFFEE4E2), onErrorContainer = Color(0xFF7A271A)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF00D2FF), onPrimary = Color(0xFF001018),
    primaryContainer = Color(0xFF003B49), onPrimaryContainer = Color(0xFF8BE9FF),
    secondary = Color(0xFF10B981), onSecondary = Color(0xFF00150E),
    secondaryContainer = Color(0xFF073C31), onSecondaryContainer = Color(0xFF8CF8CF),
    tertiary = Color(0xFF10B981), onTertiary = Color(0xFF00150E),
    background = Color(0xFF0B0F19), onBackground = Color.White,
    surface = Color(0xFF1E2640), onSurface = Color.White,
    surfaceVariant = Color(0xFF27324F), onSurfaceVariant = Color(0xFFCBD5E1),
    surfaceContainerLowest = Color(0xFF070B13),
    surfaceContainerLow = Color(0xFF101625),
    surfaceContainer = Color(0xFF151C2E),
    surfaceContainerHigh = Color(0xFF1E2640),
    surfaceContainerHighest = Color(0xFF27324F),
    outline = Color(0xFF71829C), outlineVariant = Color(0xFF334155),
    error = Color(0xFFFF8A80), onError = Color(0xFF280000),
    errorContainer = Color(0xFF4A1418), onErrorContainer = Color(0xFFFFDAD6)
)

object SolventaStatusColors {
    val success @Composable get() = MaterialTheme.colorScheme.tertiary
    val warning @Composable get() = if (MaterialTheme.colorScheme.background == DarkColors.background) Color(0xFFF59E0B) else Color(0xFFB45309)
}

@Composable
fun SolventaTheme(
    preference: ThemePreference,
    largeText: Boolean,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val dark = when (preference) {
        ThemePreference.SYSTEM -> systemDark
        ThemePreference.LIGHT -> false
        ThemePreference.DARK -> true
    }
    val view = LocalView.current
    if (!view.isInEditMode) SideEffect {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view).run {
            isAppearanceLightStatusBars = !dark
            isAppearanceLightNavigationBars = !dark
        }
    }
    val currentDensity = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(currentDensity.density, currentDensity.fontScale * if (largeText) 1.15f else 1f)
    ) {
        MaterialTheme(colorScheme = if (dark) DarkColors else LightColors, typography = Typography(), content = content)
    }
}
