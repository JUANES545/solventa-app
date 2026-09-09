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
    secondary = Color(0xFF007FA3), onSecondary = Color.White,
    background = Color(0xFFF7F9FC), onBackground = Color(0xFF0B1F3A),
    surface = Color.White, onSurface = Color(0xFF0B1F3A),
    surfaceVariant = Color(0xFFE8EEF6), onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFF64748B), error = Color(0xFFB42318), onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF00D2FF), onPrimary = Color(0xFF001018),
    secondary = Color(0xFF10B981), onSecondary = Color(0xFF00150E),
    background = Color(0xFF0B0F19), onBackground = Color.White,
    surface = Color(0xFF1E2640), onSurface = Color.White,
    surfaceVariant = Color(0xFF27324F), onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF64748B), error = Color(0xFFF87171), onError = Color(0xFF280000)
)

object SolventaStatusColors {
    val success @Composable get() = if (isSystemInDarkTheme()) Color(0xFF10B981) else Color(0xFF047857)
    val warning @Composable get() = if (isSystemInDarkTheme()) Color(0xFFF59E0B) else Color(0xFFB45309)
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
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !dark
        window.statusBarColor = android.graphics.Color.TRANSPARENT
    }
    val currentDensity = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(currentDensity.density, currentDensity.fontScale * if (largeText) 1.15f else 1f)
    ) {
        MaterialTheme(colorScheme = if (dark) DarkColors else LightColors, typography = Typography(), content = content)
    }
}
