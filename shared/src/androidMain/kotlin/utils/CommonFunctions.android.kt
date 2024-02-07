package utils

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import datamodel.model.PostModel
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Parses a date-time string in the format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" to a formatted string.
 *
 * @return The formatted date-time string.
 */
actual fun String.parseDateTime(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("MMMM d, yyyy 'at' h:mm a", Locale.ENGLISH)
    val date = inputFormat.parse(this)
    return outputFormat.format(date)
}

/**
 * Opens a URL in the default browser.
 *
 * @param url The URL to be opened.
 */
@Composable
actual fun OpenUrl(url: String) {
    val context = LocalContext.current
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    ContextCompat.startActivity(context, intent, null)
}

/**
 * Filters a list of posts to find those created within the last seven days.
 *
 * @return The list of posts created within the last seven days.
 */
actual fun List<PostModel>.findPostsCreatedInLastSevenDays(): List<PostModel> {
    val currentDate =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(
            Date(),
        )

    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val currentDateFormatted = sdf.parse(currentDate)

    val calendar = Calendar.getInstance()
    calendar.time = currentDateFormatted
    calendar.add(Calendar.DAY_OF_YEAR, -7)

    val sevenDaysAgo = calendar.time

    return this.filter { post ->
        val postDate = sdf.parse(post.createdAt)
        postDate.after(sevenDaysAgo) || postDate.equals(sevenDaysAgo)
    }
}

/**
 * Returns true as the platform is Android.
 *
 * @return True if the platform is Android, false otherwise.
 */
actual fun isAndroid(): Boolean = true


actual object CommonMethod {
    actual fun Int.toHex(): String {
        return Integer.toHexString(this).uppercase().takeLast(2).padStart(2, '0')
    }
}