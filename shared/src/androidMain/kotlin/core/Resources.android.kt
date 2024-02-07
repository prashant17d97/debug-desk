package core

/**
 * Actual implementation for handling JSON resources in Android.
 */
actual object Resources {
    /**
     * Converts a byte array representing a JSON resource into a string.
     * @param byteArray The byte array containing the JSON resource.
     * @return The JSON resource as a string.
     */
    actual fun getJsonResource(byteArray: ByteArray): String {
        return String(byteArray, Charsets.UTF_8)
    }
}

