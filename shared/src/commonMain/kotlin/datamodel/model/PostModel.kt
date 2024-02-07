package datamodel.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import utils.CommonFunctions.formattedHtml
import utils.parseDateTime

@Serializable
data class PostModel(
    val _id: String = "",
    val author: String = "",
    val authorId: String = "",
    val createdAt: String = "",
    val title: String = "",
    val subtitle: String = "",
    val thumbnail: String = "",
    private val content: String = "",
    val category: String = "",
    val categoryId: String = "",
    val popular: Boolean = false,
    val main: Boolean = false,
    val sponsored: Boolean = false,
    val likes: Int = 0,
    val views: Int = 0,
    var isSelected: Boolean = false,
) {
    val createdAtDate: String get() = createdAt.parseDateTime()

    val formattedHtmlContent: String
        @Composable
        get() = content.formattedHtml(MaterialTheme.colorScheme.background)
}
