package presentation.widgets.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Declares an expected composable function for loading web content, providing platform-specific
 * implementations for iOS and Android.
 *
 * @param modifier The modifier to apply to the WebView container.
 * @param htmlContent The HTML content to be loaded and displayed.
 */
@Composable
expect fun WebViewContainer(
    modifier: Modifier,
    htmlContent: String,
)

