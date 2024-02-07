package network.netweorkcall

import core.log.Logcat
import datamodel.model.ApiErrorCallResponse
import datamodel.model.AuthorModel
import datamodel.model.CategoryModel
import datamodel.model.HomeContent
import datamodel.model.NetworkResponse
import datamodel.model.PostComment
import datamodel.model.PostCommentRequest
import datamodel.model.PostModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Url
import io.ktor.http.contentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import network.ResponseState
import network.apiendpoints.ApiEndpointConstants
import network.apiendpoints.ApiEndpointConstants.AUTHOR
import network.apiendpoints.ApiEndpointConstants.DATE_PARAM
import network.apiendpoints.ApiEndpointConstants.GET_HOME_CONTENT
import network.apiendpoints.ApiEndpointConstants.ID
import network.apiendpoints.ApiEndpointConstants.TYPE
import network.urlbuilder.UrlBuilder
import utils.NetworkUtil.parseData


/**
 * Implementation of the [ApiCalls] interface providing methods to interact with the backend server.
 * Uses Ktor's HttpClient for making HTTP requests.
 * @property urlBuilder Responsible for building URLs for API endpoints.
 * @property ktorHttpClient HTTP client for making network requests.
 *
 *
 */
class ApiCallImpl(
    private val urlBuilder: UrlBuilder,
    private val ktorHttpClient: HttpClient,
) : ApiCalls {
    private val _responseState: MutableStateFlow<ResponseState> =
        MutableStateFlow(ResponseState.Loaded)
    override val responseState: StateFlow<ResponseState> = _responseState

    private val _responseMessage: MutableStateFlow<String> = MutableStateFlow("")
    override val responseMessage: StateFlow<String> = _responseMessage

    override fun loading() {
        _responseState.tryEmit(ResponseState.Loading)
    }

    override fun loaded() {
        _responseState.tryEmit(ResponseState.Loaded)
    }

    override fun notFound() {
        _responseState.tryEmit(ResponseState.NotFound)
    }

    override fun noData() {
        _responseState.tryEmit(ResponseState.NoData)
    }

    override fun someErrorOccurred() {
        _responseState.tryEmit(ResponseState.SomeErrorOccurred)
    }

    override fun clearMessage() {
        _responseMessage.tryEmit("")
    }

    override suspend fun getHomeContent(): NetworkResponse<HomeContent> {
        loading()
        return try {
            val response =
                urlBuilder
                    .addPathSegment(GET_HOME_CONTENT)
                    .build()
                    .getProcessor()

            NetworkResponse.Success(response.parseData())
        } catch (ex: Exception) {
            Logcat.d("ApiCallImpl", "getHomeContent Call $ex")
            NetworkResponse.Error(ApiErrorCallResponse())
        }
    }

    override suspend fun getAuthorById(authorId: String): NetworkResponse<AuthorModel> {
        loading()
        return try {
            val response =
                urlBuilder
                    .addPathSegment(AUTHOR)
                    .addQueryParam(ID, authorId)
                    .build().getProcessor()
            NetworkResponse.Success(response.parseData())
        } catch (ex: Exception) {
            Logcat.d("ApiCallImpl", "getAuthorById call $ex")
            NetworkResponse.Error(
                ApiErrorCallResponse(
                    errorMessage = "Author is not available with associated id.",
                ),
            )
        }
    }

    override suspend fun retrievePost(postId: String): NetworkResponse<PostModel> {
        loading()
        return try {
            val response =
                urlBuilder.addPathSegment(ApiEndpointConstants.POST)
                    .addQueryParam(ID, postId)
                    .build().getProcessor()
            NetworkResponse.Success(response.parseData())
        } catch (ex: Exception) {
            Logcat.d("ApiCallImpl", "retrievePost call $ex")
            NetworkResponse.Error(ApiErrorCallResponse())
        }
    }

    override suspend fun getCategoryById(categoryId: String): NetworkResponse<CategoryModel> {
        loading()
        return try {
            val response =
                urlBuilder
                    .addPathSegment(ApiEndpointConstants.CATEGORY)
                    .addQueryParam(ID, categoryId).build().getProcessor()
            NetworkResponse.Success(response.parseData())
        } catch (ex: Exception) {
            Logcat.d("ApiCallImpl", "getCategoryById call $ex")
            NetworkResponse.Error(ApiErrorCallResponse())
        }
    }

    override suspend fun retrieveCategories(): NetworkResponse<List<CategoryModel>> {
        loading()
        return try {
            val response =
                urlBuilder
                    .addPathSegment(ApiEndpointConstants.CATEGORY)
                    .build()
                    .getProcessor()
            NetworkResponse.Success(response.parseData())
        } catch (ex: Exception) {
            Logcat.d("ApiCallImpl", "getCategoryById call $ex")
            NetworkResponse.Error(ApiErrorCallResponse())
        }
    }

    override suspend fun getAuthorsPosts(
        authorId: String,
        date: String?,
    ): NetworkResponse<List<PostModel>> {
        loading()
        return try {
            val response =
                urlBuilder
                    .addPathSegment(AUTHOR)
                    .addQueryParamMap(
                        mapOf(
                            ID to authorId,
                            "posts" to "true",
                            DATE_PARAM to (date ?: ""),
                        ),
                    ).build().getProcessor()
            NetworkResponse.Success(response.parseData())
        } catch (ex: Exception) {
            Logcat.d("ApiCallImpl", "getAuthorsPosts call $ex")
            NetworkResponse.Error(ApiErrorCallResponse())
        }
    }

    override suspend fun fetchAllPost(type: String?): NetworkResponse<List<PostModel>> {
        loading()
        return try {
            val response =
                if (type != null) {
                    urlBuilder
                        .addPathSegment(ApiEndpointConstants.POST)
                        .addQueryParam(TYPE, type)
                } else {
                    urlBuilder
                        .addPathSegment(ApiEndpointConstants.POST)
                }.build().getProcessor()
            NetworkResponse.Success(response.parseData())
        } catch (ex: Exception) {
            Logcat.d("ApiCallImpl", "fetchAllPost call $ex")
            NetworkResponse.Error(ApiErrorCallResponse())
        }
    }

    override suspend fun addComment(postCommentRequest: PostCommentRequest): NetworkResponse<String> {
        loading()
        return try {
            val response =
                urlBuilder
                    .addPathSegment(ApiEndpointConstants.ADD_COMMENT)
                    .build()
                    .postProcessor(postCommentRequest)
            NetworkResponse.Success(response.parseData())
        } catch (ex: Exception) {
            Logcat.d("ApiCallImpl", "addComment call $ex")
            NetworkResponse.Error(ApiErrorCallResponse())
        }
    }

    override suspend fun getComment(postId: String): NetworkResponse<List<PostComment>> {
        loading()
        return try {
            val response =
                urlBuilder
                    .addPathSegment(ApiEndpointConstants.GET_COMMENT).addQueryParam(
                        ID,
                        postId,
                    ).build().getProcessor()
            NetworkResponse.Success(response.parseData())
        } catch (ex: Exception) {
            Logcat.d("ApiCallImpl", "getComment call $ex")
            NetworkResponse.Error(ApiErrorCallResponse())
        }
    }

    override suspend fun updateChildComment(postCommentRequest: PostCommentRequest): NetworkResponse<String> {
        loading()
        return try {
            val response =
                urlBuilder
                    .addPathSegment(ApiEndpointConstants.UPDATE_COMMENT)
                    .build()
                    .postProcessor(postCommentRequest)
            NetworkResponse.Success(response.parseData())
        } catch (ex: Exception) {
            Logcat.d("ApiCallImpl", "updateChildComment call $ex")
            NetworkResponse.Error(ApiErrorCallResponse())
        }
    }

    override suspend fun getPostsByName(searchString: String): NetworkResponse<List<PostModel>> {
        loading()
        return try {
            val response =
                urlBuilder
                    .addPathSegment(ApiEndpointConstants.POST_SEARCH)
                    .addQueryParam(ApiEndpointConstants.TITLE, searchString)
                    .build()
                    .getProcessor()
            NetworkResponse.Success(response.parseData())
        } catch (ex: Exception) {
            Logcat.d("ApiCallImpl", "getPostsByName call $ex")
            NetworkResponse.Error(ApiErrorCallResponse())
        }
    }

    override suspend fun getPostsByCategory(categoryId: String): NetworkResponse<List<PostModel>> {
        loading()
        return try {
            val response =
                urlBuilder
                    .addPathSegment(ApiEndpointConstants.NEW)
                    .addQueryParam(ApiEndpointConstants.CATEGORY, categoryId)
                    .build()
                    .getProcessor()
            NetworkResponse.Success(response.parseData())
        } catch (ex: Exception) {
            Logcat.d("ApiCallImpl", "getPostsByCategory call $ex")
            NetworkResponse.Error(ApiErrorCallResponse())
        }
    }

    override suspend fun getCategory(): NetworkResponse<List<CategoryModel>> {
        loading()
        return try {
            val response =
                urlBuilder
                    .addPathSegment(ApiEndpointConstants.CATEGORY)
                    .build()
                    .getProcessor()
            NetworkResponse.Success(response.parseData())
        } catch (ex: Exception) {
            Logcat.d("ApiCallImpl", "getCategory call $ex")
            NetworkResponse.Error(ApiErrorCallResponse())
        }
    }

    override fun updateResponseState(statusCode: Int) {
        Logcat.d("ApiCallImpl", "Response code:--->$statusCode")
        when (statusCode) {
            404 -> notFound()
            400 -> noData()
            200 -> loaded()
            else -> someErrorOccurred()
        }
    }

    override fun updateResponseMessage(responseMsg: String) {
        _responseMessage.tryEmit(responseMsg)
    }

    private suspend fun String.getProcessor(): HttpResponse {
        return ktorHttpClient.get(
            url = Url(this),
        )
    }

    private suspend inline fun <reified Generic> String.postProcessor(requestBody: Generic): HttpResponse {
        return ktorHttpClient.post(this) {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }
    }
}
