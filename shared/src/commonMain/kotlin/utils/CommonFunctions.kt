package utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import clickevents.AppTheme
import core.ResourcePath.Drawable.GITHUB
import core.ResourcePath.Drawable.INSTAGRAM
import core.ResourcePath.Drawable.LINKEDIN
import core.ResourcePath.Drawable.YOUTUBE
import core.ResourcePath.Drawable.iconGallery
import core.datastore.repo.DataStoreRepository
import datamodel.model.PostModel
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import network.apiendpoints.ApiEndpointConstants.json
import utils.CommonMethod.toHex

/**
 * Utility object containing common functions used throughout the application.
 */
object CommonFunctions {
    /**
     * Converts RGB color values to hexadecimal format.
     * @param red The red component of the color.
     * @param green The green component of the color.
     * @param blue The blue component of the color.
     * @return The hexadecimal representation of the color.
     */
    fun rgbToHex(
        red: Int,
        green: Int,
        blue: Int,
    ): String {
        // Convert RGB values to hexadecimal format...
        val hexRed = red.toHex()
        val hexGreen = green.toHex()
        val hexBlue = blue.toHex()

        return "#$hexRed$hexGreen$hexBlue"
    }

    /**
     * Converts an RGB color represented as a Color object to hexadecimal format.
     * @param contentColor The Color object representing the RGB color.
     * @return The hexadecimal representation of the color.
     */
    fun rgbFloatToHex(contentColor: Color): String {
        // Convert RGB color to hexadecimal format...
        val hexRed = (contentColor.red * 255).toInt().coerceIn(0, 255).toHex()
        val hexGreen = (contentColor.green * 255).toInt().coerceIn(0, 255).toHex()
        val hexBlue = (contentColor.blue * 255).toInt().coerceIn(0, 255).toHex()

        return "#$hexRed$hexGreen$hexBlue"
    }

    /**
     * Formats a string as HTML with specified background color and text color.
     * @param backgroundColor The background color for the HTML body.
     * @return The HTML-formatted string.
     */
    @Composable
    fun String.formattedHtml(backgroundColor: Color): String {
        // Format the string as HTML with specified background and text color...
        return """<html>
        <head>
            <style type='text/css'>pre, code { white-space: pre-wrap; }</style>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="background-color:${rgbFloatToHex(backgroundColor)}; color: ${
            rgbFloatToHex(contentColorFor(backgroundColor))
        };">
            ${
            this.replace(
                "color: #fff;",
                "color: #fff; overflow-wrap: break-word;",
            )
        }
            </body>
        </html>"""
    }

    /**
     * Determines if a string is empty or null.
     * @return The original string if not empty or null, otherwise returns null.
     */
    fun String?.isDataEmpty(): String? {
        // Check if the string is empty or null...
        return if (this.isNullOrBlank()) {
            null
        } else {
            this
        }
    }

    /**
     * Converts a Google Drive link to a format suitable for direct access.
     * @return The converted Google Drive link.
     */
    fun String.convertDriveLink(): String {
        // Convert Google Drive link format...
        val regex = Regex("uc\\?export=view&id=")
        return this.replace(regex, "uc?id=")
    }


    inline fun <reified Generic> String.parseJson(): Generic {
        return json.decodeFromString<Generic>(this)
    }

    inline fun <reified Generic> parseData(data: Generic): String {
        return json.encodeToString(data)
    }

    suspend fun List<PostModel>.filterSavedList(dataStoreRepository: DataStoreRepository): List<PostModel> {
        dataStoreRepository.getPost()
        return this.map { postModel ->
            dataStoreRepository.posts.first().find { it._id == postModel._id }?.let {
                // If a matching item is found in savedPost, update isSelected in currentList
                postModel.copy(isSelected = true)
            } ?: postModel // If no match is found, keep the original item from currentList
        }
    }

    suspend fun PostModel.filterSavedPost(dataStoreRepository: DataStoreRepository): PostModel {
        dataStoreRepository.getPost()
        val savedPost = dataStoreRepository.posts.first().find { it._id == this._id }
        return this.copy(isSelected = this._id == savedPost?._id)
    }

    fun String.getIcon(): String {
        return when (this.lowercase()) {
            "youtube" -> YOUTUBE
            "linkedin" -> LINKEDIN
            "instagram" -> INSTAGRAM
            "github" -> GITHUB
            else -> iconGallery
        }
    }

    @Composable
    fun AppTheme.isSystemInDarkMode(): Boolean {
        return when (this) {
            AppTheme.SYSTEM_DEFAULT -> isSystemInDarkTheme()
            AppTheme.LIGHT -> false
            AppTheme.DARK -> true
        }
    }
}


/**
 * Platform-specific implementation of common methods.
 */
expect object CommonMethod {
    /**
     * Converts an integer value to its hexadecimal representation.
     * @return The hexadecimal representation of the integer.
     */
    fun Int.toHex(): String
}

/**
 * Platform-specific function to parse a date-time string.
 * @return The parsed date-time string.
 */
expect fun String.parseDateTime(): String

/**
 * Platform-specific composable function to open a URL.
 * @param url The URL to be opened.
 */
@Composable
expect fun OpenUrl(url: String)

/**
 * Platform-specific function to find posts created in the last seven days.
 * @return A list of posts created in the last seven days.
 */
expect fun List<PostModel>.findPostsCreatedInLastSevenDays(): List<PostModel>

/**
 * Platform-specific function to check if the current platform is Android.
 * @return true if the platform is Android, false otherwise.
 */
expect fun isAndroid(): Boolean