package network.apiendpoints

import kotlinx.serialization.json.Json

/**
 * Constants defining API endpoints for different app-related API calls.
 */
object ApiEndpointConstants {
    // Queries parameters
    const val ID = "_id"
    const val DATE_PARAM = "date"
    const val PAGE = "page"
    const val AUTHOR_POST = "posts"
    const val TYPE = "type"
    const val TITLE = "title"
    const val NEW = "new"
    private const val POPULAR = "popular"

    // Authors
    const val AUTHOR = "author"
    val authorLink = { authorId: String -> "/$AUTHOR?$ID=$authorId" }

    // Comments
    const val ADD_COMMENT = "addcomment"
    const val GET_COMMENT = "comments"
    const val UPDATE_COMMENT = "updatecomment"

    // Categories
    const val CATEGORY = "category"
    val categoryLink = { categoryId: String -> "/$NEW?$CATEGORY=$categoryId" }

    // Delimiter
    const val DELIMITER = "/"

    // Home content
    const val GET_HOME_CONTENT = "gethomecontent"

    // Login
    const val LOGIN = "login"

    // Posts
    const val POST = "post"
    const val POST_SEARCH = "search"
    const val POPULAR_POST = "popularpost"
    const val POST_SEARCH_TITLE = "$POST_SEARCH?$TITLE="
    val PostType = { postType: String -> "$POST?$TYPE=$postType" }
    val PostsByTitle = { titles: String -> "$POST_SEARCH_TITLE$titles" }
    val PopularPost = { postType: String -> "$POPULAR?$TYPE=$postType" }

    // JSON
    val json = Json { ignoreUnknownKeys = true }
}
