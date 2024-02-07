package presentation.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import clickevents.ClickEvent
import clickevents.DataStoreEvent
import core.ResourcePath
import datamodel.model.PostModel
import navigation.Screens
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.CommonElements.cornerShape
import presentation.CommonElements.elevationValue
import presentation.CommonElements.globalClip
import presentation.widgets.shared.LoadImage

/**
 * Composable function for displaying a list of posts in a LazyColumn with customizable post cards.
 *
 * @param modifier The modifier to apply to the LazyColumn.
 * @param postCardModifier The modifier to apply to each individual post card.
 * @param posts The list of posts to display.
 * @param onClick The callback invoked when a post is clicked.
 *
 * @author Prashant Singh
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostLists(
    modifier: Modifier = Modifier,
    postCardModifier: Modifier = Modifier,
    posts: List<PostModel>,
    onClick: (ClickEvent) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            space = 10.dp,
            alignment = Alignment.CenterVertically,
        ),
        horizontalAlignment = Alignment.Start,
    ) {
        items(items = posts, key = { it._id }) { postModel ->
            Post(
                modifier = postCardModifier.animateItemPlacement(),
                post = postModel,
                onClick = onClick,
            )
        }
    }
}

/**
 * Composable function for displaying a single post with optional disappearing animation.
 *
 * @param modifier The modifier to apply to the post.
 * @param post The post to display.
 * @param onClick The callback invoked when the post is clicked.
 */
@Composable
fun DisappearingListPost(
    modifier: Modifier = Modifier,
    post: PostModel,
    onClick: (ClickEvent) -> Unit,
) {
    // AnimatedVisibility composable is used for entering and exiting animations
    AnimatedVisibility(
        visible = true,
        enter = slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(durationMillis = 500, easing = LinearEasing),
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(durationMillis = 500, easing = LinearEasing),
        ),
    ) {
        Post(
            modifier = modifier,
            post = post,
            onClick = onClick,
        )
    }
}

/**
 * Composable function for displaying a single post card.
 *
 * @param modifier The modifier to apply to the post card.
 * @param post The post to display.
 * @param onClick The callback invoked when the post card is clicked.
 *
 * @author Prashant Singh
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun Post(
    modifier: Modifier = Modifier,
    post: PostModel,
    onClick: (ClickEvent) -> Unit,
) {
    var isSelected by remember { mutableStateOf(post.isSelected) }

    // Determine the bookmark icon based on the selected state
    val bookmarkIcon by rememberUpdatedState(
        if (isSelected) {
            ResourcePath.Drawable.BookmarkIcon
        } else {
            ResourcePath.Drawable.BookmarkBorderIcon
        },
    )

    // Post card with clickable functionality
    Card(
        shape = cornerShape,
        elevation = CardDefaults.cardElevation(elevationValue),
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick(
                    ClickEvent.NavigateScreen(
                        argument = post._id,
                        screen = Screens.PostScreen,
                    ),
                )
            }
            .globalClip,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(
                space = 0.dp,
                alignment = Alignment.Start,
            ),
        ) {
            // Load post thumbnail image
            LoadImage(
                modifier = Modifier.size(120.dp),
                srcUrl = post.thumbnail,
                placeHolder = ResourcePath.Drawable.iconGallery,
                background = MaterialTheme.colorScheme.surfaceVariant,
            )
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(
                    space = 5.dp,
                    alignment = Alignment.CenterVertically,
                ),
                horizontalAlignment = Alignment.Start,
            ) {
                // Post creation date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = post.createdAtDate,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraLight,
                        ),
                        textDecoration = TextDecoration.Underline,
                    )

                    // Bookmark icon with animation
                    AnimatedContent(
                        targetState = bookmarkIcon,
                        transitionSpec = {
                            if (targetState > initialState) {
                                (slideInVertically { height -> height } + fadeIn()).togetherWith(
                                    slideOutVertically { height -> -height } + fadeOut(),
                                )
                            } else {
                                (slideInVertically { height -> -height } + fadeIn()).togetherWith(
                                    slideOutVertically { height -> height } + fadeOut(),
                                )
                            }.using(
                                SizeTransform(clip = true),
                            )
                        },
                        label = "AnimatedContent",
                    ) {
                        Image(
                            painter = painterResource(bookmarkIcon),
                            contentDescription = "Bookmark",
                            modifier = Modifier.size(24.dp).clickable {
                                val (clickEvent, selected) =
                                    if (isSelected) {
                                        ClickEvent.SaveOrRemovePost(
                                            data = post,
                                            DataStoreEvent.REMOVE,
                                        ) to false
                                    } else {
                                        ClickEvent.SaveOrRemovePost(
                                            data = post,
                                            DataStoreEvent.SAVE,
                                        ) to true
                                    }
                                onClick(clickEvent)
                                isSelected = selected
                            },
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        )
                    }
                }

                // Post title and subtitle
                Text(text = post.title, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = post.subtitle,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                )

                // Author and category view
                AuthorCatView(postModel = post, onClick = onClick)
            }
        }
    }
}
