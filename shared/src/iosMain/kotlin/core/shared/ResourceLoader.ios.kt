package core.shared

import androidx.compose.runtime.Composable
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

/**
 * ResourceLoader class for iOS platform.
 * Provides methods to read resources as strings.
 *
 * @author Prashant Singh
 */
actual class ResourceLoader {
    /**
     * Companion object containing actual resource loading function.
     */
    actual companion object {
        /**
         * Reads the content of a resource file as a string.
         * @param resourceName The name of the resource file to be read.
         * @return The content of the resource file as a string, or an empty string if the resource is not found.
         */
        @OptIn(ExperimentalForeignApi::class)
        @Composable
        actual fun readResourceAsString(resourceName: String): String {
            // Get the path to the resource file
            val path = NSBundle.mainBundle.pathForResource(resourceName, "json")
            // Read the contents of the file as a string
            return path?.let { NSString.stringWithContentsOfFile(it, NSUTF8StringEncoding, null) }
                ?: "" // Return an empty string if the resource file is not found
        }
    }
}
