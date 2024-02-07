package theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Composable function for applying the theme to the entire application.
 *
 * @param darkTheme Whether the theme should be in dark mode.
 * @param dynamicColor Whether to use dynamic color schemes based on system settings (requires Android 12 or higher).
 * @param applyStaticColor Whether to apply a static color scheme or use the dynamic color scheme provided.
 * @param content The content of the application.
 *
 * @author Prashant Singh
 */
@Composable
actual fun AppTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    applyStaticColor: Boolean,
    content: @Composable () -> Unit,
) {
    // Determine the color scheme based on input parameters and system settings
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    // Control system bars color based on the selected color scheme
    val systemUiController = rememberSystemUiController()
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            systemUiController.setSystemBarsColor(color = if (applyStaticColor) Color.Black else colorScheme.background)
            // Uncomment below lines to apply status bar color separately
            // val window = (view.context as Activity).window
            // window.statusBarColor = colorScheme.background.toArgb()
            // WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    // Apply Material theme with the determined color scheme and typography
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypo(),
        content = content,
    )
}

