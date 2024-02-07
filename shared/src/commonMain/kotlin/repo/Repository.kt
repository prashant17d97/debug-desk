package repo

import datamodel.model.AuthorModel
import datamodel.model.CategoryModel
import datamodel.model.HomeContent
import datamodel.model.PostModel
import kotlinx.coroutines.flow.StateFlow
import network.ResponseState

interface Repository {
    val responseMessage: StateFlow<String>
    val responseState: StateFlow<ResponseState>
    val homeContent: StateFlow<HomeContent?>
    val authorById: StateFlow<AuthorModel>
    val postByID: StateFlow<PostModel>
    val categoryById: StateFlow<CategoryModel?>
    val categories: StateFlow<List<CategoryModel>>

    val postsByAuthorID: StateFlow<List<PostModel>>
    val allPosts: StateFlow<List<PostModel>>
    val postsByCategory: StateFlow<List<PostModel>>
    val postsOnSearch: StateFlow<List<PostModel>>

    suspend fun getHomeContent()

    suspend fun getAuthorById(authorId: String)

    suspend fun retrievePost(postId: String)

    suspend fun postsOnSearch(searchString: String)

    suspend fun getCategoryById(categoryId: String)

    suspend fun retrieveCategories()

    suspend fun getAuthorsPosts(
        authorId: String,
        date: String?,
    )

    suspend fun fetchAllPost(type: String? = null)

    suspend fun getPostsByCategory(categoryId: String)
}
