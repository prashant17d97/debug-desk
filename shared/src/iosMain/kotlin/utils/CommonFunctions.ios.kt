package utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import datamodel.model.PostModel
import platform.Foundation.NSAttributedString
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.NSURL
import platform.Foundation.currentLocale
import platform.Foundation.systemTimeZone
import platform.Foundation.timeIntervalSince1970
import platform.Foundation.timeZoneForSecondsFromGMT
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.NSTextAlignmentJustified
import platform.UIKit.NSTextAlignmentLeft
import platform.UIKit.NSTextAlignmentNatural
import platform.UIKit.NSTextAlignmentRight
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIFont
import platform.UIKit.UILabel
import platform.UIKit.UILineBreakModeCharacterWrap
import platform.UIKit.UILineBreakModeMiddleTruncation


/**
 * - **This file represents iOS Required Common functions**
 *
 * Parses the given string to a formatted date and time string.
 * @param The input string representing the date and time.
 * @return The formatted date and time string.
 *
 * @author Prashant Singh
 */
actual fun String.parseDateTime(): String {
    // Define input and output date formats
    val inputFormat = NSDateFormatter().apply {
        dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        timeZone = NSTimeZone.timeZoneForSecondsFromGMT(0)
    }
    val outputFormat = NSDateFormatter().apply {
        dateFormat = "MMMM d, yyyy 'at' h:mm a"
        timeZone = NSTimeZone.timeZoneForSecondsFromGMT(0)
    }

    // Parse the input string and format the date
    val date = inputFormat.dateFromString(this)
    return date?.let { outputFormat.stringFromDate(it) } ?: ""
}

/**
 * Opens the given URL in the default browser.
 * @param url The URL to be opened.
 */
@Composable
actual fun OpenUrl(url: String) {
    NSURL.URLWithString(url, null)?.let { UIApplication.sharedApplication.openURL(url = it) }
}

/**
 * Finds the posts created in the last seven days from the given list.
 * @return The list of posts created in the last seven days.
 */
actual fun List<PostModel>.findPostsCreatedInLastSevenDays(): List<PostModel> {
    // Get the current date and time
    val currentDate = NSDateFormatter().apply {
        dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        locale = NSLocale.currentLocale
        timeZone = NSTimeZone.systemTimeZone
    }.stringFromDate(NSDate())

    // Calculate the timestamp for seven days ago
    val sdf = NSDateFormatter().apply {
        dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        locale = NSLocale.currentLocale
        timeZone = NSTimeZone.systemTimeZone
    }
    val currentDateMillis = (sdf.dateFromString(currentDate)?.timeIntervalSince1970 ?: 0.0) * 1000
    val sevenDaysAgoMillis = currentDateMillis - (7 * 24 * 60 * 60 * 1000)

    // Filter the list based on the creation date
    return this.filter { post ->
        val postDateMillis =
            (sdf.dateFromString(post.createdAt)?.timeIntervalSince1970 ?: 0.0) * 1000
        postDateMillis > sevenDaysAgoMillis
    }
}

/**
 * Converts the color to a UIColor object.
 * @return The UIColor representation of the color.
 */
fun Color.getColor(): UIColor {
    return UIColor(
        red = red.toDouble(),
        green = green.toDouble(),
        blue = blue.toDouble(),
        alpha = alpha.toDouble(),
    )
}

/**
 * Checks if the platform is Android.
 * @return True if the platform is Android, otherwise false.
 */
actual fun isAndroid(): Boolean = false

/**
 * Creates a UILabel with the given text and styling options.
 * @param text The text to be displayed.
 * @param modifier Modifier for the text.
 * @param style Text style options.
 * @param localContentColor Local content color.
 * @param color Text color.
 * @param fontSize Font size.
 * @param fontStyle Font style.
 * @param fontWeight Font weight.
 * @param fontFamily Font family.
 * @param letterSpacing Letter spacing.
 * @param textDecoration Text decoration.
 * @param textAlign Text alignment.
 * @param lineHeight Line height.
 * @param overflow Text overflow.
 * @param softWrap Soft wrap option.
 * @param maxLines Maximum lines.
 * @param minLines Minimum lines.
 * @param onTextLayout Callback for text layout.
 * @return A UILabel with the specified text and styling options.
 */
@ExperimentalMultiplatform
fun IosText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle,
    localContentColor: Color,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int? = null,
    minLines: Int? = null,
    onTextLayout: (TextLayoutResult) -> Unit = {},
): UILabel {
    // Determine text alignment and line break mode
    val numberOfLines = minLines ?: maxLines ?: Int.MAX_VALUE
    val textureWrapMode = if (softWrap) {
        UILineBreakModeCharacterWrap
    } else {
        when (overflow) {
            TextOverflow.Clip -> UILineBreakModeCharacterWrap
            TextOverflow.Ellipsis -> UILineBreakModeMiddleTruncation
            else -> UILineBreakModeCharacterWrap
        }
    }

    // Determine text color
    val textColor = color.takeOrElse {
        style.color.takeOrElse {
            localContentColor
        }
    }

    // Apply text styling
    val mergedStyle = style.merge(
        TextStyle(
            color = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign,
            lineHeight = lineHeight,
            fontFamily = fontFamily,
            textDecoration = textDecoration,
            fontStyle = fontStyle,
            letterSpacing = letterSpacing,
        ),
    )

    // Determine text alignment for UILabel
    val textAlignment = when (mergedStyle.textAlign) {
        TextAlign.Center -> NSTextAlignmentCenter
        TextAlign.Start -> NSTextAlignmentLeft
        TextAlign.Justify -> NSTextAlignmentJustified
        TextAlign.End -> NSTextAlignmentRight
        else -> NSTextAlignmentNatural
    }

    // Create and configure the UILabel
    return UILabel().apply {
        this.text = text
        this.textColor = textColor.getColor()
        setFont(
            UIFont.systemFontOfSize(
                fontSize = mergedStyle.fontSize.value.toDouble(),
                weight = mergedStyle.fontWeight?.weight?.toDouble() ?: 400.0,
            )
        )
        this.numberOfLines = numberOfLines.toLong()
        lineBreakMode = textureWrapMode
        setTextAlignment(textAlignment)
        attributedText = NSAttributedString()
    }
}

actual object CommonMethod {
    actual fun Int.toHex(): String {
        val hexChars = "0123456789ABCDEF"
        val hexStringBuilder = StringBuilder()
        var value = this
        while (value > 0) {
            val remainder = value % 16
            hexStringBuilder.insert(0, hexChars[remainder])
            value /= 16
        }
        if (hexStringBuilder.isEmpty()) {
            hexStringBuilder.append("0")
        }

        return hexStringBuilder.toString()
    }
}