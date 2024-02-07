package presentation.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import clickevents.ClickEvent
import datamodel.model.AuthorModel
import datamodel.model.PostComment
import datamodel.model.PostModel
import presentation.widgets.shared.WebViewContainer

/**
 * Composable function representing the container for the post screen.
 *
 * @param modifier The modifier for this composable.
 * @param htmlString The HTML content string to be displayed in a WebView.
 * @param postModel The data model representing the post.
 * @param allPosts The list of all posts.
 * @param comments The list of comments associated with the post.
 * @param authorModel The data model representing the author of the post.
 * @param onClick The callback function to handle click events.
 */
@Composable
fun PostScreenContainer(
    modifier: Modifier = Modifier,
    htmlString: String,
    postModel: PostModel,
    allPosts: List<PostModel>,
    comments: List<PostComment>,
    authorModel: AuthorModel,
    onClick: (ClickEvent) -> Unit
) {
    Column(
        modifier =
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement =
        Arrangement.spacedBy(
            space = 10.dp,
            alignment = Alignment.Top,
        ),
    ) {
        AboveWebView(postModel = postModel, onClick = onClick)
        WebViewContainer(modifier = Modifier, htmlString)
        BottomWebView(
            modifier = Modifier,
            posts = allPosts,
            authorModel = authorModel,
            onClick = onClick,
        )
    }
}
