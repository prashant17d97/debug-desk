package repo

import core.datastore.repo.DataStoreRepository
import datamodel.model.AuthorModel
import datamodel.model.CategoryModel
import datamodel.model.HomeContent
import datamodel.model.PostModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import network.ResponseState
import network.netweorkcall.ApiCalls
import utils.CommonFunctions.filterSavedList
import utils.CommonFunctions.filterSavedPost
import utils.NetworkUtil.handleResponse

class RepositoryImpl(
    private val apiCalls: ApiCalls,
    private val dataStoreRepository: DataStoreRepository,
) : Repository {
    override val responseMessage: StateFlow<String> = apiCalls.responseMessage
    override val responseState: StateFlow<ResponseState> = apiCalls.responseState

    private val _homeContent: MutableStateFlow<HomeContent?> = MutableStateFlow(null)
    override val homeContent: StateFlow<HomeContent?> = _homeContent

    private val _authorById: MutableStateFlow<AuthorModel> = MutableStateFlow(AuthorModel())
    override val authorById: StateFlow<AuthorModel> = _authorById

    private val _postByID: MutableStateFlow<PostModel> = MutableStateFlow(PostModel())
    override val postByID: StateFlow<PostModel> = _postByID

    private val _categoryById: MutableStateFlow<CategoryModel?> = MutableStateFlow(null)
    override val categoryById: StateFlow<CategoryModel?> = _categoryById

    private val _postsByAuthorID: MutableStateFlow<List<PostModel>> = MutableStateFlow(emptyList())
    override val postsByAuthorID: StateFlow<List<PostModel>> = _postsByAuthorID

    private val _allPosts: MutableStateFlow<List<PostModel>> = MutableStateFlow(emptyList())
    override val allPosts: StateFlow<List<PostModel>> = _allPosts

    private val _postsOnSearch: MutableStateFlow<List<PostModel>> = MutableStateFlow(emptyList())
    override val postsOnSearch: StateFlow<List<PostModel>> = _postsOnSearch

    private val _categories: MutableStateFlow<List<CategoryModel>> =
        MutableStateFlow(
            emptyList(),
        )
    override val categories: StateFlow<List<CategoryModel>> = _categories

    private val _postsByCategory: MutableStateFlow<List<PostModel>> =
        MutableStateFlow(emptyList())
    override val postsByCategory: StateFlow<List<PostModel>> =
        _postsByCategory

    override suspend fun getHomeContent() {
        apiCalls.getHomeContent()
            .handleResponse(
                apiCalls = apiCalls,
                onSuccess = { apiCallResponse ->
                    if (apiCallResponse.statusCode == 200 && apiCallResponse.data._id.isNotEmpty()) {
                        _homeContent.tryEmit(apiCallResponse.data)
                    } else {
                        apiCalls.noData()
                    }
                },
            )
    }

    override suspend fun getAuthorById(authorId: String) {
        apiCalls.getAuthorById(authorId)
            .handleResponse(
                apiCalls = apiCalls,
                onSuccess = { apiCallResponse ->
                    if (apiCallResponse.statusCode == 200 && apiCallResponse.data._id.isNotEmpty()) {
                        _authorById.tryEmit(apiCallResponse.data)
                    } else {
                        apiCalls.noData()
                    }
                },
            )
    }

    override suspend fun retrievePost(postId: String) {
        apiCalls.retrievePost(postId)
            .handleResponse(
                apiCalls = apiCalls,
                onSuccess = { apiCallResponse ->
                    if (apiCallResponse.statusCode == 200 && apiCallResponse.data._id.isNotEmpty()) {
                        _postByID.tryEmit(apiCallResponse.data.filterSavedPost(dataStoreRepository))
                    } else {
                        apiCalls.noData()
                    }
                },
            )
    }

    override suspend fun postsOnSearch(searchString: String) {
        apiCalls.getPostsByName(searchString = searchString)
            .handleResponse(
                apiCalls = apiCalls,
                onSuccess = { apiCallResponse ->
                    val postModelList =
                        if (apiCallResponse.statusCode == 200 && apiCallResponse.data.isNotEmpty()) {
                            apiCallResponse.data.filterSavedList(dataStoreRepository)
                        } else {
                            apiCalls.noData()
                            emptyList()
                        }
                    _postsOnSearch.tryEmit(postModelList)
                },
            )
    }

    override suspend fun getCategoryById(categoryId: String) {
        apiCalls.getCategoryById(categoryId)
            .handleResponse(
                apiCalls = apiCalls,
                onSuccess = { apiCallResponse ->
                    if (apiCallResponse.statusCode == 200 && apiCallResponse.data._id.isNotEmpty()) {
                        _categoryById.tryEmit(apiCallResponse.data)
                    } else {
                        apiCalls.noData()
                    }
                },
            )
    }

    override suspend fun retrieveCategories() {
        apiCalls.retrieveCategories()
            .handleResponse(
                apiCalls = apiCalls,
                onSuccess = { apiCallResponse ->
                    if (apiCallResponse.statusCode == 200 && apiCallResponse.data.isNotEmpty()) {
                        val arrayList =
                            arrayListOf(
                                CategoryModel(
                                    _id = "",
                                    thumbnail = "",
                                    category = "For you",
                                    description = "All the latest posts",
                                ),
                                CategoryModel(
                                    _id = "",
                                    thumbnail = "",
                                    category = "Popular",
                                    description = "All the popular in this week",
                                ),
                            )
                        arrayList.addAll(apiCallResponse.data)
                        _categories.tryEmit(arrayList)
                    } else {
                        apiCalls.noData()
                    }
                },
            )
    }

    override suspend fun getAuthorsPosts(
        authorId: String,
        date: String?,
    ) {
        apiCalls.getAuthorsPosts(authorId, date)
            .handleResponse(
                apiCalls = apiCalls,
                onSuccess = { apiCallResponse ->
                    if (apiCallResponse.statusCode == 200 && apiCallResponse.data.isNotEmpty()) {
                        _postsByAuthorID.tryEmit(
                            apiCallResponse.data.filterSavedList(
                                dataStoreRepository,
                            ),
                        )
                    } else {
                        apiCalls.noData()
                    }
                },
            )
    }

    override suspend fun fetchAllPost(type: String?) {
        apiCalls.fetchAllPost(type = type)
            .handleResponse(
                apiCalls = apiCalls,
                onSuccess = { apiCallResponse ->
                    if (apiCallResponse.statusCode == 200 && apiCallResponse.data.isNotEmpty()) {
                        _allPosts.tryEmit(apiCallResponse.data.filterSavedList(dataStoreRepository))
                    } else {
                        apiCalls.noData()
                    }
                },
            )
    }

    override suspend fun getPostsByCategory(categoryId: String) {
        apiCalls.fetchAllPost()
            .handleResponse(
                apiCalls = apiCalls,
                onSuccess = { apiCallResponse ->
                    if (apiCallResponse.statusCode == 200 && apiCallResponse.data.isNotEmpty()) {
                        _postsByCategory.tryEmit(
                            apiCallResponse.data.filterSavedList(
                                dataStoreRepository,
                            ).filter { it.categoryId == categoryId },
                        )
                    } else {
                        apiCalls.noData()
                    }
                },
            )
    }
}
