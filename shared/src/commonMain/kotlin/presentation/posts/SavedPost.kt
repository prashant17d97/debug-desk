package presentation.posts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import core.NavHostController
import datamodel.model.PostModel
import network.ResponseState
import org.koin.compose.koinInject
import presentation.widgets.AppColumn
import presentation.widgets.PostLists

/**
 * Composable function representing the screen displaying saved posts.
 *
 * This composable displays a list of saved posts retrieved from the SavedPostViewModel.
 *
 * @param navHostController The navigation controller used for navigating between screens.
 *
 * @author Prashant Singh
 */
@Composable
fun SavedPost(navHostController: NavHostController) {
    // Initialize SavedPostViewModel using Koin dependency injection
    val savedPostViewModel: SavedPostViewModel = koinInject()

    // Collect saved posts and response state from the ViewModel
    val posts: List<PostModel> by savedPostViewModel.posts.collectAsState()
    val responseState by rememberUpdatedState(if (posts.isEmpty()) ResponseState.NoData else ResponseState.Loaded)

    // Retrieve saved posts when the composable is first launched
    LaunchedEffect(Unit) {
        savedPostViewModel.getSavedPost()
    }

    // Column to display the saved post screen content
    AppColumn(
        responseState = responseState,
        onRetry = {},
    ) {
        // Display the list of saved posts
        PostLists(posts = posts, onClick = {
            savedPostViewModel.handleClickEvent(it, navHostController)
        })
    }
}

