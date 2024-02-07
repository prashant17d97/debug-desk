package presentation.widgets.shared

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIScreen
import platform.WebKit.WKWebView
import platform.WebKit.javaScriptEnabled
import utils.CommonFunctions
import utils.getColor

/**
 * LottieAnimationView composable function for iOS platform.
 * This function loads and displays a Lottie animation using HTML, CSS, and JavaScript within a WKWebView.
 * @param modifier The modifier for the WKWebView.
 * @param lottieJsonSrc The JSON source of the Lottie animation.
 * @author Prashant Singh
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun LottieAnimationView(
    modifier: Modifier,
    lottieJsonSrc: String,
) {
    // Get the background color from MaterialTheme
    val background = MaterialTheme.colorScheme.background

    // HTML content to display the Lottie animation
    val webViewContent = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <!-- Include Lottie Web library -->
            <script src="https://cdnjs.cloudflare.com/ajax/libs/bodymovin/5.7.9/lottie.min.js"></script>
        </head>
        <body >
            <!-- Container to display Lottie animation -->
            <div id="lottie-container" style="background:${
        CommonFunctions.rgbFloatToHex(
            background.copy(
                alpha = 0.0f,
            ),
        )
    };"></div>

            <!-- Script to load and play the Lottie animation -->
            <script>
                const lottiePlayer = lottie.loadAnimation({
                    container: document.getElementById('lottie-container'),
                    renderer: 'svg', // 'canvas' or 'svg'
                    loop: true,
                    autoplay: true,
                    animationData: $lottieJsonSrc,
                    rendererSettings: {
                        preserveAspectRatio: 'xMidYMid meet', // Ensure correct aspect ratio
                        progressiveLoad: true, // Load frames progressively
                        hideOnTransparent: true, // Hide elements when they are transparent
                        className: 'lottie-animation', // Add a class to the animation container
                    },
                });
            </script>
        </body>
        </html>
    """

    // Create a WKWebView to display the Lottie animation
    UIKitView(
        modifier = modifier,
        factory = {
            WKWebView(frame = UIScreen.mainScreen.bounds).also {
                it.opaque = false
                it.backgroundColor = background.getColor()
                it.configuration.preferences.javaScriptEnabled = true
                it.scrollView.bounces = false
            }
        },
        update = {
            it.loadHTMLString(webViewContent, baseURL = null)
        },
    )
}
