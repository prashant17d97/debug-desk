package presentation.post

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import clickevents.ClickEvent
import core.ResourcePath
import core.ResourcePath.Drawable.TOTAL_VIEWED
import datamodel.model.PostModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.widgets.AuthorCatView
import presentation.widgets.shared.LoadImage

/**
 * Composable function representing the content above the WebView in the post screen.
 *
 * @param modifier The modifier for this composable.
 * @param postModel The data model representing the post.
 * @param onClick The callback function to handle click events.
 *
 * @author Prashant Singh
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun AboveWebView(
    modifier: Modifier = Modifier,
    postModel: PostModel,
    onClick: (ClickEvent) -> Unit,
) {
    var isSelected by remember { mutableStateOf(false) }

    // Determine the appropriate like icon based on the current selection state
    val likeIcon by rememberUpdatedState(
        if (isSelected) {
            ResourcePath.Drawable.LIKED
        } else {
            ResourcePath.Drawable.LIKED_BORDER
        },
    )

    Column(
        modifier = modifier.fillMaxWidth().padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 10.dp,
            alignment = Alignment.Top,
        ),
    ) {
        // Display post title and subtitle
        Text(text = postModel.title, style = MaterialTheme.typography.headlineMedium)
        Text(text = postModel.subtitle, style = MaterialTheme.typography.headlineSmall)

        // Display author and category information
        AuthorCatView(postModel = postModel, onClick = onClick)
        Text(text = postModel.createdAtDate)

        // Display like, view count, and thumbnail image
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space = 10.dp,
                alignment = Alignment.Start,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Animated like icon with click functionality
            AnimatedContent(
                targetState = likeIcon,
                transitionSpec = {
                    // Compare the incoming number with the previous number.
                    if (targetState > initialState) {
                        // If the target number is larger, it slides up and fades in
                        // while the initial (smaller) number slides up and fades out.
                        (slideInVertically { height -> height } + fadeIn()).togetherWith(
                            slideOutVertically { height -> -height } + fadeOut(),
                        )
                    } else {
                        // If the target number is smaller, it slides down and fades in
                        // while the initial number slides down and fades out.
                        (slideInVertically { height -> -height } + fadeIn()).togetherWith(
                            slideOutVertically { height -> height } + fadeOut(),
                        )
                    }.using(
                        // Disable clipping since the faded slide-in/out should
                        // be displayed out of bounds.
                        SizeTransform(clip = true),
                    )
                },
                label = "AnimatedContent",
            ) {
                Icon(
                    painter = painterResource(likeIcon),
                    contentDescription = it,
                    modifier = Modifier.size(24.dp).clickable {
                        isSelected = !isSelected
                    },
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
            Text(text = "${postModel.likes}", style = MaterialTheme.typography.bodyMedium)

            // Display view count
            Icon(
                painter = painterResource(TOTAL_VIEWED),
                contentDescription = TOTAL_VIEWED,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(text = "${postModel.views}", style = MaterialTheme.typography.bodyMedium)
        }

        // Load thumbnail image
        LoadImage(
            modifier = Modifier.fillMaxWidth().height(250.dp).padding(top = 20.dp),
            srcUrl = postModel.thumbnail,
            placeHolder = ResourcePath.Drawable.iconGallery,
            background = MaterialTheme.colorScheme.background,
        )
    }
}
