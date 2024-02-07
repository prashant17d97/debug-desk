package core

import androidx.compose.ui.unit.dp

/**
 * A utility object that holds constants representing various sizes used in the application.
 *
 * @author Prashant Singh
 */
object Size {
    /**
     * Width of the logo in DP.
     */
    val logoWidth = 175.dp

    /**
     * Height of the logo in DP.
     */
    val logoHeight = 139.dp

    /**
     * Height of the welcome icon in DP.
     */
    val welcomeIconHeight = 350.dp

    /**
     * Object holding constants for button sizes.
     */
    object Button {
        /**
         * Height of the button in DP.
         */
        val height = 60.dp

        /**
         * Width of the button in DP.
         */
        val width = 160.dp

        /**
         * Height of the back button in DP.
         */
        val backButton = 40.dp
    }

    /**
     * Object holding constants for padding sizes.
     */
    object Padding {
        /**
         * Horizontal padding for the parent layout in DP.
         */
        val parentHorizontal = 15.dp

        /**
         * Vertical padding for the parent layout in DP.
         */
        val parentVertical = 20.dp
    }
}
