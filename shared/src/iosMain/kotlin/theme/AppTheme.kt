package theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect

/**
 * AppTheme composable function for iOS platform.
 * This function defines the overall theme of the app for iOS devices.
 * @param darkTheme Boolean value indicating whether the dark theme should be applied.
 * @param dynamicColor Boolean value indicating whether dynamic color changes are enabled.
 * @param applyStaticColor Boolean value indicating whether to apply static colors.
 * @param content The content to be displayed within the theme.
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
    // Determine the color scheme based on the darkTheme parameter
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    SideEffect {
//        UIApplication.sharedApplication.setStatusBarStyle(UIStatusBarStyleBlackTranslucent)
//        UIApplication.sharedApplication.setStatusBarStyle(UIStatusBarStyleDarkContent)
    }

    // Apply the MaterialTheme with the defined color scheme and typography
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypo(),
        content = content,
    )
}

