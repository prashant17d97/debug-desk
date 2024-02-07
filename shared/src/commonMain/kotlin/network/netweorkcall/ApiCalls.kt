package network.netweorkcall

import datamodel.model.AuthorModel
import datamodel.model.CategoryModel
import datamodel.model.HomeContent
import datamodel.model.NetworkResponse
import datamodel.model.PostComment
import datamodel.model.PostCommentRequest
import datamodel.model.PostModel
import kotlinx.coroutines.flow.StateFlow
import network.ResponseState

/**
 * Interface defining API calls for interacting with the backend server.
 * All methods provide suspend functions for asynchronous execution and
 * return [NetworkResponse] wrapping the response data.
 * Additionally, the interface provides state flow objects [responseState]
 * and [responseMessage] to observe the API call status and messages.
 *
 * @author Prashant Singh
 */
interface ApiCalls {
    // Properties
    val responseState: StateFlow<ResponseState> // State flow object to observe the API call status.
    val responseMessage: StateFlow<String> // State flow object to observe the API call messages.

    // Methods

    /**
     * Notifies the API call state as loading.
     */
    fun loading()

    /**
     * Notifies the API call state as loaded.
     */
    fun loaded()

    /**
     * Notifies the API call state as not found.
     */
    fun notFound()

    /**
     * Notifies the API call state as no data.
     */
    fun noData()

    /**
     * Notifies the API call state as some error occurred.
     */
    fun someErrorOccurred()

    /**
     * Clears the API call message.
     */
    fun clearMessage()

    /**
     * Updates the API call response state based on the provided HTTP status code.
     * @param statusCode The HTTP status code of the response.
     */
    fun updateResponseState(statusCode: Int)

    /**
     * Updates the API call response message.
     * @param responseMsg The response message.
     */
    fun updateResponseMessage(responseMsg: String)

    // API calls

    /**
     * Retrieves home content from the server.
     */
    suspend fun getHomeContent(): NetworkResponse<HomeContent>

    /**
     * Retrieves author details by ID from the server.
     * @param authorId The ID of the author.
     */
    suspend fun getAuthorById(authorId: String): NetworkResponse<AuthorModel>

    /**
     * Retrieves post details by ID from the server.
     * @param postId The ID of the post.
     */
    suspend fun retrievePost(postId: String): NetworkResponse<PostModel>

    /**
     * Retrieves posts by name from the server.
     * @param searchString The search string to filter posts.
     */
    suspend fun getPostsByName(searchString: String): NetworkResponse<List<PostModel>>

    /**
     * Retrieves posts by category from the server.
     * @param categoryId The ID of the category.
     */
    suspend fun getPostsByCategory(categoryId: String): NetworkResponse<List<PostModel>>

    /**
     * Retrieves category details by ID from the server.
     * @param categoryId The ID of the category.
     */
    suspend fun getCategoryById(categoryId: String): NetworkResponse<CategoryModel>

    /**
     * Retrieves all available categories from the server.
     */
    suspend fun retrieveCategories(): NetworkResponse<List<CategoryModel>>

    /**
     * Retrieves posts authored by a specific author from the server.
     * @param authorId The ID of the author.
     * @param date Optional date parameter for filtering posts.
     */
    suspend fun getAuthorsPosts(authorId: String, date: String?): NetworkResponse<List<PostModel>>

    /**
     * Fetches all posts from the server.
     * @param type Optional type parameter for filtering posts.
     */
    suspend fun fetchAllPost(type: String? = null): NetworkResponse<List<PostModel>>

    /**
     * Adds a comment to a post on the server.
     * @param postCommentRequest The request object containing the comment details.
     */
    suspend fun addComment(postCommentRequest: PostCommentRequest): NetworkResponse<String>

    /**
     * Retrieves comments for a specific post from the server.
     * @param postId The ID of the post.
     */
    suspend fun getComment(postId: String): NetworkResponse<List<PostComment>>

    /**
     * Updates a child comment on the server.
     * @param postCommentRequest The request object containing the updated comment details.
     */
    suspend fun updateChildComment(postCommentRequest: PostCommentRequest): NetworkResponse<String>

    /**
     * Retrieves all available categories from the server.
     */
    suspend fun getCategory(): NetworkResponse<List<CategoryModel>>
}
