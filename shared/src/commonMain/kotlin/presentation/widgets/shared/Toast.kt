package presentation.widgets.shared

import androidx.compose.runtime.Composable

/**
 * Declares an expected composable function for displaying toast messages, providing platform-specific
 * implementations for iOS and Android.
 *
 * On Android, this function will show a toast message using the native Toast API.
 * On iOS, the implementation will utilize platform-specific APIs for displaying toast-like messages.
 *
 * @param message The message to be displayed in the toast.
 */
@Composable
expect fun ToastMsg(message: String)

