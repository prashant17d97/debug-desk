package presentation.widgets.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import platform.Foundation.NSHTTPURLResponse
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSURL
import platform.Foundation.NSURLSession
import platform.Foundation.dataTaskWithRequest
import platform.UIKit.UIActivityIndicatorView
import platform.UIKit.UIActivityIndicatorViewStyleMedium
import platform.UIKit.UIImage
import platform.UIKit.UIImageView
import platform.UIKit.UIViewContentMode
import presentation.CommonElements.globalClip
import utils.getColor
import kotlin.coroutines.resume

/**
 * LoadImage composable function for iOS platform.
 * This function loads an image from a URL asynchronously and displays it in a UIImageView.
 * It also provides placeholder and loading indicator support.
 * @param modifier The modifier for the image.
 * @param srcUrl The URL of the image to be loaded.
 * @param placeHolder The resource name of the placeholder image.
 * @param background The background color of the image container.
 * @param enter The enter transition animation for the image.
 * @param exit The exit transition animation for the image.
 * @param loader The custom loading indicator composable.
 * @author Prashant Singh
 */
@OptIn(ExperimentalForeignApi::class, ExperimentalResourceApi::class)
@Composable
actual fun LoadImage(
    modifier: Modifier,
    srcUrl: String,
    placeHolder: String,
    background: Color,
    enter: EnterTransition,
    exit: ExitTransition,
    loader: @Composable() (() -> Unit)?,
) {
    // State variables to track loading and error states
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    // Create a UIImageView to display the image
    val imageView = remember {
        UIImageView().apply {
            contentMode = UIViewContentMode.UIViewContentModeScaleAspectFill
            layer.masksToBounds = false
            backgroundColor = background.getColor()
            translatesAutoresizingMaskIntoConstraints = true
        }
    }

    // Use LaunchedEffect to fetch the image asynchronously
    LaunchedEffect(srcUrl) {
        try {
            // Download the image from the URL
            val image = ImageDownloader().downloadImage(srcUrl)
            isLoading = false

            // Check if the image is successfully loaded
            if (image != null) {
                isError = false
                imageView.image = image
            } else {
                isError = true
            }
        } catch (e: Exception) {
            // Handle exceptions (e.g., network errors)
            isLoading = false
            isError = true
        }
    }

    // Display the placeholder image when loading or error occurs
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        // Show placeholder image if loading or error occurred
        AnimatedVisibility(visible = !isLoading && isError, enter = enter, exit = exit) {
            Image(
                painter = painterResource(placeHolder),
                contentDescription = "Placeholder",
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop,
            )
        }

        // Show loading indicator while fetching the image
        AnimatedVisibility(visible = isLoading && !isError, enter = enter, exit = exit) {
            // Use custom loader or default UIActivityIndicatorView
            loader ?: UIKitView(
                factory = {
                    UIActivityIndicatorView().apply {
                        activityIndicatorViewStyle = UIActivityIndicatorViewStyleMedium
                        hidesWhenStopped = true
                        backgroundColor = background.getColor()
                        startAnimating()
                    }
                },
                modifier = Modifier.size(40.dp)
            )
        }

        // Show the loaded image if no error occurred
        AnimatedVisibility(visible = !isLoading && !isError, enter = enter, exit = exit) {
            UIKitView(
                modifier = modifier.globalClip,
                factory = { imageView },
                update = {
                    // Set constraints for centering the image
                    it.superview?.let { it1 ->
                        it.centerXAnchor.constraintEqualToAnchor(it1.centerXAnchor).setActive(true)
                    }
                    it.superview?.centerYAnchor?.let { it1 ->
                        it.centerYAnchor.constraintEqualToAnchor(it1).setActive(true)
                    }
                    it.contentMode = UIViewContentMode.UIViewContentModeScaleAspectFill
                },
            )
        }
    }
}

/**
 * ImageDownloader class for iOS platform.
 * This class provides functionality to download images asynchronously.
 * @property coroutineDispatcher The coroutine dispatcher for running the download operation.
 * @author Prashant Singh
 */
class ImageDownloader(private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main) {
    /**
     * Suspended function to download an image from the specified URL.
     * @param urlString The URL of the image to download.
     * @return The downloaded UIImage, or null if the download fails.
     */
    suspend fun downloadImage(urlString: String): UIImage? = withContext(coroutineDispatcher) {
        val url = NSURL.URLWithString(urlString)
        val request = url?.let { NSMutableURLRequest.requestWithURL(it) }

        return@withContext suspendCancellableCoroutine { continuation ->
            if (request != null) {
                // Initiate a data task to download the image
                NSURLSession.sharedSession.dataTaskWithRequest(
                    request,
                    completionHandler = { data, response, error ->
                        if (error != null) {
                            continuation.resume(null)
                            return@dataTaskWithRequest
                        }

                        if (response != null && response is NSHTTPURLResponse &&
                            (200 until 300).contains(response.statusCode.toInt())
                        ) {
                            if (data != null) {
                                // Convert the downloaded data to UIImage
                                val image = UIImage.imageWithData(data)
                                continuation.resume(image)
                            } else {
                                continuation.resume(null)
                            }
                        } else {
                            continuation.resume(null)
                        }
                    },
                ).resume()
            }
        }
    }
}
