package presentation.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import clickevents.ClickEvent
import datamodel.model.PostModel
import navigation.Screens

/**
 * Composable function for displaying the author and category information of a post.
 *
 * @param postModel The model containing the details of the post.
 * @param onClick The action to perform when the author or category text is clicked.
 *
 * @author Prashant Singh
 */
@Composable
fun AuthorCatView(
    postModel: PostModel,
    onClick: (ClickEvent) -> Unit,
) {
    // Row to horizontally arrange author and category information
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 2.dp,
            alignment = Alignment.CenterHorizontally,
        ),
    ) {
        // Text indicating "By"
        Text(text = "By", style = MaterialTheme.typography.labelSmall)

        // Text displaying the author's name with clickable behavior to navigate to author details
        Text(
            text = postModel.author,
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier.clickable {
                onClick.invoke(
                    ClickEvent.NavigateScreen(
                        argument = postModel.authorId,
                        screen = Screens.Author,
                    ),
                )
            },
        )

        // Text indicating "at"
        Text(text = "at", style = MaterialTheme.typography.labelSmall)

        // Text displaying the category with clickable behavior to navigate to category details
        Text(
            text = postModel.category,
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier.clickable {
                onClick.invoke(
                    ClickEvent.NavigateScreen(
                        argument = postModel.categoryId,
                        screen = Screens.Category,
                    ),
                )
            },
        )
    }
}
