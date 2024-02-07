package presentation.widgets.shared

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


/**
 * Composable function for displaying a toast message.
 * This function displays a toast message with the specified [message].
 *
 * @param message The message to be displayed in the toast.
 *
 * @author Prashant Singh
 */
@Composable
actual fun ToastMsg(message: String) {
    // Retrieve the current context
    val localContext = LocalContext.current
    // Show the toast message
    Toast.makeText(localContext, message, Toast.LENGTH_SHORT).show()
}
