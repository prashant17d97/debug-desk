package theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import core.ResourcePath.Font.VigaRegularFont
import core.font

/**
 * Defines typography styling for the application, providing text styles for various components and sizes.
 * The [fontFamily] function specifies the font family to be used across different font weights and styles.
 * The [AppTypo] function defines the actual typography settings using the specified font family and
 * text styles for different components and sizes.
 *
 * @author Prashant Singh
 */
@Composable
fun fontFamily(): FontFamily {
    // FontFamily.Default
    return FontFamily(
        font(
            res = VigaRegularFont,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
        ),
        font(
            res = VigaRegularFont,
            fontWeight = FontWeight.SemiBold,
            fontStyle = FontStyle.Normal,
        ),
        font(
            res = VigaRegularFont,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
        ),
        font(
            res = VigaRegularFont,
            fontWeight = FontWeight.ExtraBold,
            fontStyle = FontStyle.Normal,
        ),
        font(
            res = VigaRegularFont,
            fontWeight = FontWeight.Light,
            fontStyle = FontStyle.Normal,
        ),
        font(
            res = VigaRegularFont,
            fontWeight = FontWeight.ExtraLight,
            fontStyle = FontStyle.Normal,
        ),
    )
}

/**
 * Defines typography settings for the application, specifying text styles for various components and sizes.
 * This function utilizes the [fontFamily] function to specify the font family and applies different text styles using the defined font family.
 * @return Typography settings object containing text styles for different components and sizes.
 */

@Composable
fun AppTypo() =
    Typography(
        displayLarge =
            TextStyle(
                fontFamily = fontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 57.sp,
                lineHeight = 64.sp,
                letterSpacing = 0.5.sp,
            ),
        displayMedium =
            TextStyle(
                fontFamily = fontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 45.sp,
                lineHeight = 52.sp,
                letterSpacing = 0.5.sp,
            ),
        displaySmall =
            TextStyle(
                fontFamily = fontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 36.sp,
                lineHeight = 44.sp,
                letterSpacing = 0.5.sp,
            ),
        headlineLarge =
            TextStyle(
                fontFamily = fontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.5.sp,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = fontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                lineHeight = 36.sp,
                letterSpacing = 0.5.sp,
            ),
        headlineSmall =
            TextStyle(
                fontFamily = fontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.5.sp,
            ),
        titleLarge =
            TextStyle(
                fontFamily = fontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.5.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = fontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
        titleSmall =
            TextStyle(
                fontFamily = fontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.5.sp,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = fontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = fontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.5.sp,
            ),
        bodySmall =
            TextStyle(
                fontFamily = fontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
            ),
        labelLarge =
            TextStyle(
                fontFamily = fontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.5.sp,
            ),
        labelMedium =
            TextStyle(
                fontFamily = fontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 26.sp,
                letterSpacing = 0.5.sp,
            ),
        labelSmall =
            TextStyle(
                fontFamily = fontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
            ),
    )
