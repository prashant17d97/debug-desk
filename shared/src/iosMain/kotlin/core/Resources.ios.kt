package core

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString

/**
 * Resources object for iOS platform.
 * Provides methods to handle JSON resources.
 *
 * @author Prashant Singh
 */
actual object Resources {

    /**
     * Reads the content of a JSON resource from a byte array and returns it as a string.
     * @param byteArray The byte array containing the JSON resource data.
     * @return The content of the JSON resource as a string.
     */
    @OptIn(ExperimentalForeignApi::class)
    actual fun getJsonResource(byteArray: ByteArray): String {
        // Convert the byte array to a Kotlin string
        return byteArray.toKString()
    }
}

