package core

import kotlinx.coroutines.flow.StateFlow
import navigation.Screens

/**
 * Defines an interface for controlling navigation within a navigation host.
 * It provides functionality to navigate to different screens, manage the back stack, and handle navigation arguments.
 *
 * @property NavHostController The interface for controlling navigation within a navigation host.
 *
 * @property currentStack A [StateFlow] representing the current screen stack.
 * @property backStackEntry A [StateFlow] representing the back stack of screens.
 * @property isPushingUp A [StateFlow] representing whether navigation is in progress.
 * @property canExit A [StateFlow] representing whether the navigation controller can exit.
 * @property getArguments A [StateFlow] representing the arguments for navigation.
 *
 *
 * @author Prashant Singh
 */
interface NavHostController {
    val currentStack: StateFlow<Screens>
    val backStackEntry: StateFlow<List<Screens>>
    val isPushingUp: StateFlow<Boolean>
    val canExit: StateFlow<Boolean>
    val getArguments: StateFlow<String>

    /**
     * Navigates to the specified screen with optional arguments.
     *
     * @param route The destination screen to navigate to.
     * @param popInclusive A flag indicating whether to pop the back stack inclusively.
     * @param navigatingForward A flag indicating the direction of navigation.
     * @param argumentString The string representation of arguments for navigation.
     */
    fun navigate(
        route: Screens,
        popInclusive: Boolean = false,
        navigatingForward: Boolean = true,
        argumentString: String? = null,
    )

    /**
     * Pops up the back stack, navigating to the previous screen.
     */
    fun popUp()
}
