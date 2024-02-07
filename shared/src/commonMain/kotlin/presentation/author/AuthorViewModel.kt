package presentation.author

import kotlinx.coroutines.launch
import main.MainViewModel
import repo.Repository

/**
 * ViewModel responsible for managing author-related data.
 * @param repository The repository used for fetching author data.
 * @author Prashant Singh
 */

class AuthorViewModel(
    private val repository: Repository,
) : MainViewModel() {

    // StateFlow representing the author model
    val authorModel = repository.authorById

    // StateFlow representing the posts by the author
    val authorPosts = repository.postsByAuthorID

    /**
     * Function to fetch author data by ID.
     * @param authorID The ID of the author to fetch.
     */
    fun getAuthor(authorID: String) {
        viewModelScope.launch {
            repository.getAuthorById(authorID)
        }
    }

    /**
     * Function to fetch posts by the author.
     * @param authorID The ID of the author whose posts to fetch.
     */
    fun getAuthorPost(authorID: String) {
        viewModelScope.launch {
            repository.getAuthorsPosts(authorID, null)
        }
    }
}

