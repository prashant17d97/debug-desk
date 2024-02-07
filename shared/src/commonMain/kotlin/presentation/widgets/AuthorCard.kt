package presentation.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import clickevents.ClickEvent
import core.ResourcePath
import datamodel.model.AuthorModel
import navigation.Screens
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.widgets.shared.LoadImage
import utils.CommonFunctions.getIcon
import utils.OpenUrl

/**
 * Composable function for displaying author details, including their image, name, and social links.
 *
 * @param modifier The modifier to apply to the author card.
 * @param authorModel The model containing the details of the author.
 * @param onClick The action to perform when the author card is clicked.
 *
 * @author Prashant Singh
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun AuthorCard(
    modifier: Modifier = Modifier,
    authorModel: AuthorModel,
    onClick: (ClickEvent) -> Unit,
) {
    // Retrieve the background color from the current theme
    val backgroundColor by rememberUpdatedState(MaterialTheme.colorScheme.background)

    // State to track the URL to be opened when a social link is clicked
    var openUrl: String? by remember { mutableStateOf(null) }

    // Composable column to display author information
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = 10.dp, alignment = Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Load author image
        LoadImage(
            modifier = modifier.size(100.dp).clip(CircleShape),
            srcUrl = authorModel.userImage,
            placeHolder = ResourcePath.Drawable.iconGallery,
            background = backgroundColor,
        )

        // Display author name with clickable behavior to navigate to author details
        Text(
            modifier = Modifier.clickable {
                onClick(
                    ClickEvent.NavigateScreen(
                        screen = Screens.Author,
                        argument = authorModel._id,
                    ),
                )
            },
            text = authorModel.name,
            style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.primary),
        )

        // Text indicating to follow the author on social media
        Text(
            text = "Follow me on my social handles below",
        )

        // Row to display social media icons
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = 5.dp,
                alignment = Alignment.CenterHorizontally,
            ),
        ) {
            // Iterate over social links and display respective icons
            authorModel.socialLinks.forEach {
                Image(
                    painter = painterResource(it.platform.getIcon()),
                    contentDescription = it.platform,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.size(30.dp).clickable {
                        openUrl = it.platformLink
                    },
                )
            }
        }
    }

    // Open URL when a social link is clicked
    openUrl?.let { OpenUrl(it) }
}

