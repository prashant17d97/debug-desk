package core

import androidx.compose.runtime.Composable

/**
 * Defines an expect function for handling the back press event.
 *
 * @param enabled A flag indicating whether the back press handling is enabled.
 * @param onBack The callback function to be invoked when the back press event occurs.
 *
 * @author Prashant Singh
 */
@Composable
expect fun BackPressHandler(enabled: Boolean, onBack: () -> Unit)

/**
 * Defines an expect function for closing the application.*
 *
 * @author Prashant Singh
 */
@Composable
expect fun CloseApplication()
