package presentation.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import core.ResourcePath
import core.ResourcePath.Drawable.contentDescription
import core.Size
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

/**
 * Composable function for displaying a column with a gradient background and an image overlay.
 *
 * @param modifier The modifier to apply to the column.
 * @param verticalArrangement The vertical arrangement strategy for the content of the column.
 * @param horizontalAlignment The horizontal alignment strategy for the content of the column.
 * @param vertical The vertical padding to apply to the column.
 * @param horizontal The horizontal padding to apply to the column.
 * @param image The image resource to use as an overlay on the column.
 * @param colors The list of colors to use for the gradient background.
 * @param brush The brush used to define the gradient background.
 * @param content The content of the column.
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun GradiantWithImageColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    vertical: Dp = Size.Padding.parentVertical,
    horizontal: Dp = Size.Padding.parentHorizontal,
    image: String = ResourcePath.Drawable.splashPattern,
    colors: List<Color> =
        listOf(
            Color.Transparent,
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.background,
        ),
    brush: Brush =
        Brush.verticalGradient(
            colors = colors,
        ),
    content: @Composable ColumnScope.() -> Unit,
) {
    // Box to contain the image overlay and the column content
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        // Image overlay
        Image(
            painter = painterResource(image),
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
            contentDescription = ResourcePath.Drawable.splashPattern.contentDescription,
        )

        // Column with gradient background
        Column(
            modifier =
            modifier
                .fillMaxSize()
                .background(
                    brush = brush,
                )
                .padding(horizontal, vertical),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content,
        )
    }
}
