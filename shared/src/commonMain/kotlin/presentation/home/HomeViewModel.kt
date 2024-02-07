package presentation.home

import core.datastore.repo.DataStoreRepository
import kotlinx.coroutines.launch
import main.MainViewModel
import repo.Repository

/**
 * ViewModel responsible for managing data related to the Home screen.
 *
 * @property repository The repository used for fetching data related to posts and categories.
 * @property dataStoreRepository The repository used for data storage operations.
 *
 * @author Prashant Singh
 */
class HomeViewModel(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository,
) : MainViewModel() {
    // StateFlow representing the list of posts
    val posts = repository.allPosts

    // StateFlow representing the list of categories
    val categories = repository.categories

    init {
        // Initialization block to retrieve categories and fetch posts when necessary
        viewModelScope.launch {
            // Retrieve categories and fetch posts if categories list is empty
            retrieveCategories()
            categories.collect {
                if (it.isEmpty()) {
                    fetchPosts("For you")
                }
            }
        }
    }

    /**
     * Function to fetch posts based on a specified type.
     *
     * @param type The type of posts to fetch.
     */
    fun fetchPosts(type: String) {
        // Launching a coroutine to fetch posts asynchronously
        viewModelScope.launch {
            // Fetch posts from the repository
            repository.fetchAllPost(
                type = type,
            )
        }
    }

    /**
     * Function to get posts by category ID.
     *
     * @param id The ID of the category.
     */
    private fun getPostByCategoryID(id: String) {
        // Launching a coroutine to get posts by category ID asynchronously
        viewModelScope.launch {
            // Get posts by category ID from the repository
            repository.getPostsByCategory(id)
        }
    }

    /**
     * Function to retrieve categories.
     */
    fun retrieveCategories() {
        // Launching a coroutine to retrieve categories asynchronously
        viewModelScope.launch {
            // Retrieve categories from the repository
            repository.retrieveCategories()
        }
    }
}

