package navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import core.NavHostController
import presentation.Splash
import presentation.about.About
import presentation.account.Account
import presentation.author.Author
import presentation.category.Category
import presentation.home.Home
import presentation.post.PostScreen
import presentation.posts.SavedPost
import presentation.search.Search



/**
 * Composable function representing the navigation graph of the application.
 * It defines transitions between different screens based on the current navigation state.
 *
 * @param navHostController The navigation controller responsible for managing navigation actions.
 *
 * @see NavHostController
 *
 * @author Prashant Singh
 */
@Composable
fun NavGraph(navHostController: NavHostController) {
    // Collecting the current navigation stack and pushing-up state
    val currentStack by navHostController.currentStack.collectAsState()
    val isPushingUp by navHostController.isPushingUp.collectAsState()

    // Defining transition animations based on the current navigation state
    AnimatedContent(
        targetState = currentStack,
        transitionSpec = {
            (
                    fadeIn() +
                            slideInHorizontally(
                                animationSpec = tween(400),
                                initialOffsetX = { fullHeight ->
                                    fullHeight.takeIf { isPushingUp } ?: -fullHeight
                                },
                            )
                    ).togetherWith(
                    fadeOut(
                        animationSpec = tween(300),
                    ),
                )
        },
    ) { targetState ->
        // Rendering the content based on the target navigation state
        when (targetState) {
            Screens.Splash -> Splash(navHostController)
            Screens.Home -> Home(navHostController)
            Screens.Search -> Search(navHostController)
            Screens.SavedPost -> SavedPost(navHostController)
            Screens.Account -> Account(navHostController)
            Screens.PostScreen -> PostScreen(navHostController)
            Screens.Author -> Author(navHostController)
            Screens.Category -> Category(navHostController)
            Screens.About -> About(navHostController)
        }
    }
}

/**
 * Experimental composable function defining transitions between different states of a composable.
 * This function is currently experimental and the work is in progress.
 *
 * @param targetState The target state of the composable.
 * @param isPushingUp Flag indicating whether the content is pushing up or not.
 * @param content The content of the composable.
 *
 * @see AnimatedContent
 * @see Screens
 * @see NavHostController
 *
 * @author Prashant Singh
 */
@ExperimentalAnimationApi
@Composable
private fun composable(
    targetState: Screens,
    isPushingUp: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedContent(
        targetState = targetState,
        transitionSpec = {
            (
                    fadeIn() +
                            slideInHorizontally(
                                animationSpec = tween(400),
                                initialOffsetX = { fullHeight ->
                                    fullHeight.takeIf { isPushingUp } ?: -fullHeight
                                },
                            )
                    ).togetherWith(
                    fadeOut(
                        animationSpec = tween(200),
                    ),
                )
        },
    ) {
        content()
    }
}

