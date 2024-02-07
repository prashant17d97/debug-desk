package presentation.account

import clickevents.DataStoreEvent
import navigation.Screens

/**
 * Sealed class representing different events that can occur within the app's menu.
 *
 * @author Prashant Singh
 */
sealed class MenuEvent {
    /**
     * Event to navigate to a specific screen.
     * @property argument Optional argument for navigation.
     * @property screen The destination screen to navigate to.
     * @property popInclusive Flag indicating whether to pop the current screen inclusively.
     * @property navigatingForward Flag indicating the direction of navigation.
     */
    data class NavigateScreen(
        val argument: String? = null,
        val screen: Screens,
        val popInclusive: Boolean = false,
        val navigatingForward: Boolean = true,
    ) : MenuEvent()

    /**
     * Event to change the app's theme.
     */
    data object Theme : MenuEvent()

    /**
     * Event to save or remove a post.
     * @property theme Boolean indicating whether to save or remove.
     * @property event DataStoreEvent representing the event type.
     */
    data class SaveOrRemovePost(val theme: Boolean, val event: DataStoreEvent) : MenuEvent()
}


