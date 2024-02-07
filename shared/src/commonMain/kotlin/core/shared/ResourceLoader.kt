package core.shared

import androidx.compose.runtime.Composable
import core.shared.ResourceLoader.Companion.readResourceAsString


/**
 * Defines an expect class for loading resources.
 * This class should be implemented in platform-specific code to provide actual resource loading functionality.
 *
 * @property ResourceLoader The expect class for loading resources.
 *
 * @property readResourceAsString A composable function to read a resource file as a string.
 *
 * @return The content of the resource file as a string.
 *
 * @author Prashant Singh
 */
expect class ResourceLoader {
    companion object {

        /**
         * Reads the content of a resource file as a string.
         *
         * @param resourceName The name of the resource file to be read.
         * @return The content of the resource file as a string.
         */
        @Composable
        fun readResourceAsString(resourceName: String): String
    }
}
