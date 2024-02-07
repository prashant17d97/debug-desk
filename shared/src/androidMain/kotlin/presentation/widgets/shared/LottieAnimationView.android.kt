package presentation.widgets.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

/**
 * Composable function for displaying a Lottie animation.
 * This function loads the Lottie animation from either a network URL or a local JSON string and renders it on the screen.
 *
 * @param modifier The modifier for the composable.
 * @param lottieJsonSrc The source of the Lottie animation, which can be either a network URL or a JSON string.
 *
 * @author Prashant Singh
 */
@Composable
actual fun LottieAnimationView(modifier: Modifier, lottieJsonSrc: String) {
    // Load the Lottie composition based on the source (URL or JSON string)
    val composition: LottieComposition? by rememberLottieComposition(
        if (lottieJsonSrc.contains("http")) {
            LottieCompositionSpec.Url(
                lottieJsonSrc
            )
        } else {
            LottieCompositionSpec.JsonString(
                lottieJsonSrc
            )
        }
    )

    // Display the Lottie animation
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )
}
