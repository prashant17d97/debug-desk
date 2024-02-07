package presentation.post

import core.datastore.repo.DataStoreRepository
import kotlinx.coroutines.launch
import main.MainViewModel
import repo.CommentProcessor
import repo.Repository

class PostScreenVIewModel(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository,
    private val commentProcessor: CommentProcessor,
) : MainViewModel() {
    val post = repository.postByID
    val allPosts = repository.allPosts
    val postComment = commentProcessor.comments
    val author = repository.authorById

    init {
        getPosts()
    }

    fun retrievePost(postId: String) {
        viewModelScope.launch {
            repository.retrievePost(postId)
        }
    }

    private fun getPosts() {
        viewModelScope.launch {
            repository.fetchAllPost()
        }
    }

    fun getPostComment(postId: String) {
        viewModelScope.launch {
            commentProcessor.getCommentFrontEnd(postId)
        }
    }

    fun getAuthorById(authorId: String) {
        viewModelScope.launch {
            repository.getAuthorById(authorId)
        }
    }
}
