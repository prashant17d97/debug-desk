package presentation.widgets.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.CommonElements.globalClip

/**
 * Composable function for loading an image from a network URL using the Coil library.
 * This function handles various cases such as loading, success, and error states, and displays a placeholder
 * image in case of errors or while the image is being loaded.
 *
 * @param modifier The modifier for the composable.
 * @param srcUrl The URL of the image to load.
 * @param placeHolder The resource identifier of the placeholder image to display in case of errors or while loading.
 * @param background The background color of the image container.
 * @param enter The enter transition animation.
 * @param exit The exit transition animation.
 * @param loader Optional loader composable to display while the image is loading.
 *
 * @author Prashant Singh
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun LoadImage(
    modifier: Modifier,
    srcUrl: String,
    placeHolder: String,
    background: Color,
    enter: EnterTransition,
    exit: ExitTransition,
    loader: @Composable (() -> Unit)?,
) {
    var isLoading: Boolean by remember { mutableStateOf(true) }

    // Define the painter for loading the image asynchronously with Coil
    val painter =
        rememberAsyncImagePainter(
            model = srcUrl,
            error = painterResource(placeHolder),
            fallback = painterResource(placeHolder),
            onLoading = {
                isLoading = true
            },
            onSuccess = {
                isLoading = false
            },
            onError = {
                isLoading = false
            },
        )

    // Compose the layout to display the image and loader
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        // Image composable to display the loaded image
        Image(
            painter = painter,
            contentDescription = "Image",
            modifier = modifier.globalClip.then(
                if (!isLoading) {
                    Modifier.size(40.dp)
                } else {
                    Modifier
                },
            ),
            contentScale = ContentScale.Crop,
        )

        // Animated visibility to show loader while loading
        AnimatedVisibility(visible = isLoading, enter = enter, exit = exit) {
            loader ?: CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

