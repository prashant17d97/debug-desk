package presentation.widgets.shared

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Declares an expected composable function for loading an image, providing platform-specific implementations
 * for iOS and Android.
 *
 * On Android, this function will be implemented using Jetpack Compose's `Image` composable.
 * On iOS, the implementation will utilize platform-specific APIs for loading and displaying images.
 *
 * @param modifier The modifier for the composable.
 * @param srcUrl The URL or resource identifier of the image to load.
 * @param placeHolder The resource identifier for the placeholder image to be displayed while the main image is loading.
 * @param background The background color for the image container.
 * @param enter The enter transition animation for the image.
 * @param exit The exit transition animation for the image.
 * @param loader An optional composable function to customize the loader UI while the image is loading.
 */
@Composable
expect fun LoadImage(
    modifier: Modifier = Modifier,
    srcUrl: String,
    placeHolder: String,
    background: Color,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    loader: @Composable (() -> Unit)? = null,
)

