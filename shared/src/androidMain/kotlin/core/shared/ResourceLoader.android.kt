package core.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

actual class ResourceLoader {
    /**
     * Actual implementation of resource loading functionality for Android.
     * @author Prashant Singh
     */
    actual companion object {
        /**
         * Reads the content of a raw resource file and returns it as a String.
         * @param resourceName The name of the resource file.
         * @return The content of the resource file as a String.
         */
        @Composable
        actual fun readResourceAsString(resourceName: String): String {
            val context = LocalContext.current
            // Get the resource ID using the resource name and package name
            val resourceId = context.resources.getIdentifier(resourceName, "raw", context.packageName)
            // Open the raw resource file as a BufferedReader and read its text
            return context.resources.openRawResource(resourceId).bufferedReader().use {
                it.readText()
            }
        }
    }
}
