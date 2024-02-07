package presentation.widgets.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Declares an expected composable function for displaying Lottie animations, providing platform-specific
 * implementations for iOS and Android.
 *
 * On Android, this function will be implemented using the Lottie library to load and play the animation.
 * On iOS, the implementation will utilize platform-specific APIs for loading and displaying Lottie animations.
 *
 * @param modifier The modifier for the composable.
 * @param lottieJsonSrc The JSON source string or file path of the Lottie animation.
 */
@Composable
expect fun LottieAnimationView(
    modifier: Modifier,
    lottieJsonSrc: String,
)

