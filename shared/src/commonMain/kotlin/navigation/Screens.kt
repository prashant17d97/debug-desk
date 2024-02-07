package navigation

/**
 * Constants defining the keys for various screens in the navigation graph.
 */
const val SPLASH_SCREEN = "Splash"
const val HOME_SCREEN = "Home"
const val SEARCH_SCREEN = "SearchScreen"
const val SAVED_POSTS = "SavedPosts"
const val ACCOUNT_SCREEN = "AccountScreen"
const val POST_SCREEN = "PostScreen"
const val AUTHOR = "Author"
const val CATEGORY = "Category"
const val ABOUT = "About"

/**
 * Sealed class representing the screens in the navigation graph.
 * Each screen has a title, icon resource id, route name, and a flag indicating whether it's navigating back.
 *
 * @param title The title of the screen.
 * @param icon The icon resource id for the screen.
 * @param route The route name for navigation.
 * @param isNavigatingBack Flag indicating whether the screen is navigating back.
 */
sealed class Screens(
    var title: String,
    var icon: Int = 0,
    var route: String = title,
    var isNavigatingBack: Boolean = false,
) {
    // Splash screen
    data object Splash : Screens(title = SPLASH_SCREEN)

    // Home screen
    data object Home : Screens(title = HOME_SCREEN)

    // Search screen
    data object Search : Screens(title = SEARCH_SCREEN)

    // Saved posts screen
    data object SavedPost : Screens(title = SAVED_POSTS)

    // Account screen
    data object Account : Screens(title = ACCOUNT_SCREEN)

    // Post screen
    data object PostScreen : Screens(title = POST_SCREEN)

    // Author screen
    data object Author : Screens(title = AUTHOR)

    // Category screen
    data object Category : Screens(title = CATEGORY)

    // About screen
    data object About : Screens(title = ABOUT)
}
