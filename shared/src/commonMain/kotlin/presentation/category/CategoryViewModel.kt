package presentation.category

import core.datastore.repo.DataStoreRepository
import kotlinx.coroutines.launch
import main.MainViewModel
import repo.Repository

class CategoryViewModel(
    val repository: Repository,
    private val dataStoreRepository: DataStoreRepository,
) : MainViewModel() {
    val post = repository.postsByCategory
    val category = repository.categoryById

    fun getPostOnCategory(categoryID: String) {
        viewModelScope.launch {
            repository.getPostsByCategory(categoryID)
        }
    }

    fun getCategory(categoryID: String) {
        viewModelScope.launch {
            repository.getCategoryById(categoryID)
        }
    }
}
