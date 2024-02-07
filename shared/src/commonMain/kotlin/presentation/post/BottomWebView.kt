package presentation.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import clickevents.ClickEvent
import datamodel.model.AuthorModel
import datamodel.model.PostModel
import navigation.Screens
import presentation.widgets.AuthorCard
import presentation.widgets.Post

/**
 * Composable function representing the content below the WebView in the post screen.
 *
 * @param modifier The modifier for this composable.
 * @param posts The list of post models to display.
 * @param authorModel The author model for the post.
 * @param onClick The callback function to handle click events.
 */
@Composable
fun BottomWebView(
    modifier: Modifier = Modifier,
    posts: List<PostModel>,
    authorModel: AuthorModel,
    onClick: (ClickEvent) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 10.dp,
            alignment = Alignment.Top,
        ),
    ) {
        // Display author information
        AuthorCard(
            modifier = Modifier,
            authorModel = authorModel,
            onClick = onClick,
        )

        // Row for displaying "You might also like..." text and "More" button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "You might also like...",
            )
            Text(
                text = "More",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    onClick.invoke(
                        ClickEvent.NavigateScreen(
                            screen = Screens.Home,
                            popInclusive = true,
                        ),
                    )
                },
            )
        }

        // Display the list of posts
        posts.forEach {
            Post(
                modifier = Modifier,
                post = it,
                onClick = onClick,
            )
        }
    }
}

