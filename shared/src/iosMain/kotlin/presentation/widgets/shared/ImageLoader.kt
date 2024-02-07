package presentation.widgets.shared

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSHTTPURLResponse
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSURL
import platform.Foundation.NSURLSession
import platform.Foundation.dataTaskWithRequest
import platform.Foundation.getBytes
import kotlin.coroutines.resume

/*
@OptIn(ExperimentalResourceApi::class)
@Composable
fun Image() {
    val imageDownloadResult: ImageDownloadResult by remember { mutableStateOf(ImageDownloadResult.Loading) }
    LaunchedEffect(Unit) {
        ImageLoader().downloadImage("")
    }
    when (val result = imageDownloadResult) {
        ImageDownloadResult.Error -> painterResource("")
        ImageDownloadResult.Loading -> painterResource("")
        is ImageDownloadResult.Success -> BitmapPainter(result.imageData.)
    }

    androidx.compose.foundation.Image(painter = painterResource(), "")
}*/

/**
 * Represents the result of an image download operation.
 * @author Prashant Singh
 */
sealed class ImageDownloadResult {
    /** Indicates that the image download operation is in progress. */
    data object Loading : ImageDownloadResult()

    /** Indicates that the image download operation was successful, containing the image data. */
    data class Success(val imageData: ByteArray) : ImageDownloadResult()

    /** Indicates that an error occurred during the image download operation. */
    data object Error : ImageDownloadResult()
}

/**
 * Class responsible for loading images from network URLs on the iOS platform.
 * @param coroutineDispatcher The coroutine dispatcher to use for the image loading operation.
 * @author Prashant Singh
 *
 */
class ImageLoader(private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main) {
    /**
     * Asynchronously downloads an image from the specified URL.
     * @param urlString The URL of the image to download.
     * @return An [ImageDownloadResult] representing the result of the image download operation.
     */
    suspend fun downloadImage(urlString: String): ImageDownloadResult =
        withContext(coroutineDispatcher) {
            val url = NSURL.URLWithString(urlString)
            val request = url?.let { NSMutableURLRequest.requestWithURL(it) }

            return@withContext suspendCancellableCoroutine { continuation ->
                if (request != null) {
                    NSURLSession.sharedSession.dataTaskWithRequest(
                        request,
                        completionHandler = { data, response, error ->
                            if (error != null) {
                                continuation.resume(ImageDownloadResult.Error)
                                return@dataTaskWithRequest
                            }

                            if (response != null && response is NSHTTPURLResponse &&
                                (200 until 300).contains(response.statusCode.toInt())
                            ) {
                                if (data != null) {
                                    val byteArray = data.toByteArray()
                                    continuation.resume(ImageDownloadResult.Success(byteArray))
                                } else {
                                    continuation.resume(ImageDownloadResult.Error)
                                }
                            } else {
                                continuation.resume(ImageDownloadResult.Error)
                            }
                        },
                    ).resume()
                }
            }
        }
}

/**
 * Extension function to convert an NSData object to a ByteArray.
 * @return The ByteArray representation of the NSData object.
 */
@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    val length = this.length.toInt()
    val byteArray = ByteArray(length)
    this.getBytes(this.bytes, this.length)
    return byteArray
}
