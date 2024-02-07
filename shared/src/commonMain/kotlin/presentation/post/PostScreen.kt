package presentation.post

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import clickevents.ClickEvent
import core.NavHostController
import navigation.Screens
import org.koin.compose.koinInject
import presentation.widgets.AppColumn

/**
 * Composable function representing the post screen.
 *
 * This composable displays a post along with its details, comments, and related posts.
 *
 * @param navHostController The navigation controller used for navigating between screens.
 *
 * @author Prashant Singh
 */
@Composable
fun PostScreen(navHostController: NavHostController) {
    // Retrieve postId from navigation arguments
    val postId by navHostController.getArguments.collectAsState()

    // Initialize PostScreenViewModel using Koin dependency injection
    val postScreenVIewModel: PostScreenVIewModel = koinInject()

    // Collect states from the ViewModel
    val post by postScreenVIewModel.post.collectAsState()
    val allPosts by postScreenVIewModel.allPosts.collectAsState()
    val comments by postScreenVIewModel.postComment.collectAsState()
    val responseState by postScreenVIewModel.responseState.collectAsState()
    val authorModel by postScreenVIewModel.author.collectAsState()

    // State to hold postId when navigating from "More" button
    var postIdFromMoreToSee: String? by remember { mutableStateOf(null) }

    // Retrieve post details and comments when postId changes
    LaunchedEffect(Unit) {
        postScreenVIewModel.retrievePost(postId)
    }

    // Retrieve post details when navigating from "More" button
    LaunchedEffect(postIdFromMoreToSee) {
        postIdFromMoreToSee?.let { postScreenVIewModel.retrievePost(it) }
    }

    // Retrieve author details and comments when post details are available
    LaunchedEffect(post) {
        if (post.authorId.isNotEmpty()) {
            postScreenVIewModel.getAuthorById(post.authorId)
            postScreenVIewModel.getPostComment(postId)
        }
    }

    // Column to display the post screen content
    AppColumn(
        responseState = responseState,
        modifier = Modifier.fillMaxSize(),
        onRetry = { postScreenVIewModel.retrievePost(postId) },
    ) {
        // If post details are available, display the post screen container
        if (post._id.isNotEmpty()) {
            PostScreenContainer(
                modifier = Modifier.fillMaxSize(),
                postModel = post,
                htmlString = post.formattedHtmlContent,
                allPosts = allPosts.take(4),
                comments = comments,
                authorModel = authorModel,
            ) { clickEvent ->
                // Handle click events
                when (clickEvent) {
                    is ClickEvent.NavigateScreen -> {
                        when (clickEvent.screen) {
                            Screens.PostScreen -> postIdFromMoreToSee = clickEvent.argument
                            else ->
                                postScreenVIewModel.handleClickEvent(
                                    clickEvent,
                                    navHostController,
                                )
                        }
                    }

                    else -> postScreenVIewModel.handleClickEvent(
                        clickEvent,
                        navHostController,
                    )
                }
            }
        }
    }
}

