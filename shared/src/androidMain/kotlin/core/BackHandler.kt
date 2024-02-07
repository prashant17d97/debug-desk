package core

import android.app.Activity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner

/**
 * Actual implementation of back press handler for Android.
 * @param enabled Indicates whether the back press handling is enabled.
 * @param onBack The callback to invoke when the back button is pressed.
 *
 * @author Prashant Singh
 */
@Composable
actual fun BackPressHandler(
    enabled: Boolean,
    onBack: () -> Unit,
) {
    val currentOnBack by rememberUpdatedState(onBack)
    // Create a new OnBackPressedCallback to handle back button presses
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBack() // Invoke the provided callback when back button is pressed
            }
        }
    }
    // Ensure that the backCallback's isEnabled state is updated when enabled changes
    SideEffect {
        backCallback.isEnabled = enabled
    }
    // Retrieve the OnBackPressedDispatcher from the current context
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current
    // Add the backCallback to the backDispatcher when the composable is disposed
    DisposableEffect(lifecycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose {
            backCallback.remove() // Remove the backCallback when the composable is disposed
        }
    }
}

/**
 * Actual implementation to close the application for Android.
 * Closes the current activity and finishes the app.
 */
@Composable
actual fun CloseApplication() {
    val context = LocalContext.current as Activity
    context.finishAffinity() // Finish the activity and all activities immediately below it in the stack
}
