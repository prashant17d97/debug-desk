package presentation.author

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.NavHostController
import org.koin.compose.koinInject
import presentation.widgets.AppColumn
import presentation.widgets.AuthorCard
import presentation.widgets.Post
import utils.findPostsCreatedInLastSevenDays

/**
 * Composable function to display author information and their posts.
 * @param navHostController The NavHostController used for navigation.
 *
 * @author Prashant Singh
 */
@Composable
fun Author(navHostController: NavHostController) {
    // Inject ViewModel using Koin
    val authorViewModel: AuthorViewModel = koinInject()

    // Collecting author ID from NavHostController
    val authorID by navHostController.getArguments.collectAsState()

    // Collecting author model, author posts, and response state from ViewModel
    val authorModel by authorViewModel.authorModel.collectAsState()
    val authorPosts by authorViewModel.authorPosts.collectAsState()
    val responseState by authorViewModel.responseState.collectAsState()

    // Fetch author data when author ID is available
    LaunchedEffect(Unit) {
        if (authorID.isNotEmpty() && authorModel._id.isEmpty()) {
            authorViewModel.getAuthor(authorID)
        }
    }

    // Fetch author posts when author ID is available
    LaunchedEffect(authorModel._id) {
        if (authorModel._id.isNotEmpty()) {
            authorViewModel.getAuthorPost(authorModel._id)
        }
    }

    // Compose UI for displaying author information and posts
    AppColumn(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        responseState = responseState,
        verticalArrangement = Arrangement.spacedBy(space = 10.dp, alignment = Alignment.Top),
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Display author card if author information is available
        AnimatedVisibility(authorModel._id.isNotEmpty()) {
            AuthorCard(
                modifier = Modifier,
                authorModel = authorModel,
            ) { authorViewModel.handleClickEvent(it, navHostController) }
        }

        // Display recent posts
        AnimatedVisibility(authorPosts.isNotEmpty()) {
            Column(
                verticalArrangement =
                Arrangement.spacedBy(
                    space = 10.dp,
                    alignment = Alignment.Top,
                ),
            ) {
                if (authorPosts.findPostsCreatedInLastSevenDays().isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Recent",
                        style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.primary),
                    )
                    authorPosts.findPostsCreatedInLastSevenDays().forEach { postModel ->
                        Post(post = postModel) {
                            authorViewModel.handleClickEvent(it, navHostController)
                        }
                    }
                }

                // Display popular posts
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Popular",
                    style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.primary),
                )
                authorPosts.filter { it.likes > 300 }.forEach { postModel ->
                    Post(post = postModel) {
                        authorViewModel.handleClickEvent(it, navHostController)
                    }
                }

                // Display all posts
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "All",
                    style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.primary),
                )
                authorPosts.forEach { postModel ->
                    Post(post = postModel) {
                        authorViewModel.handleClickEvent(it, navHostController)
                    }
                }
            }
        }
    }
}
