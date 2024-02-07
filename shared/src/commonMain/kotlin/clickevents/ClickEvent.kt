package clickevents

import datamodel.model.PostModel
import navigation.Screens


/**
 * Represents various click events that can occur within the application, including navigation to a screen,
 * saving or removing a post, updating the application theme, and toggling the use of the system palette.
 *
 * @author Prashant Singh
 */
sealed class ClickEvent {

    /**
     * Represents a navigation event within the application, which may include an optional argument,
     * the target screen to navigate to, whether to pop inclusively while navigating, and the direction of navigation.
     *
     * @param argument An optional argument to pass along with the navigation event.
     * @param screen The target screen to navigate to.
     * @param popInclusive Indicates whether to pop inclusive while navigating.
     * @param navigatingForward Indicates whether the navigation is forward or backward.
     *
     */
    data class NavigateScreen(
        val argument: String? = null,
        val screen: Screens,
        val popInclusive: Boolean = false,
        val navigatingForward: Boolean = true,
    ) : ClickEvent()

    /**
     * Represents an event to save or remove a post within the application, including the post data
     * and the type of data store event (SAVE or REMOVE).
     *
     * @param data The post data associated with the event.
     * @param event The type of data store event (SAVE or REMOVE) to perform on the post.
     *
     */
    data class SaveOrRemovePost(val data: PostModel, val event: DataStoreEvent) : ClickEvent()

    /**
     * Represents an event to save the application theme.
     *
     * @param appTheme The theme to be saved or applied to the application.
     *
     */
    data class SaveTheme(val appTheme: AppTheme) : ClickEvent()

    /**
     * Represents an event to toggle the use of the system palette within the application.
     *
     * @param isUsingSystemPalette Indicates whether the system palette should be used or not.
     *
     */
    data class UseSystemPalette(val isUsingSystemPalette: Boolean = false) : ClickEvent()
}

/**
 * Represents the types of events that can occur on data stored within the application.
 */
enum class DataStoreEvent {
    SAVE,
    REMOVE,
}

/**
 * Represents the different themes that the application can use.
 *
 * @property text The textual representation of the theme.
 *
 * @param text The textual representation of the theme.
 */
enum class AppTheme(val text: String) {
    SYSTEM_DEFAULT("System Default"),
    DARK("Dark"),
    LIGHT("Light"),
}
