package presentation.search

import core.datastore.repo.DataStoreRepository
import kotlinx.coroutines.launch
import main.MainViewModel
import repo.Repository

class SearchViewModel(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository,
) : MainViewModel() {
    val postsOnSearch = repository.postsOnSearch

    fun postOnSearch(searchString: String) {
        viewModelScope.launch {
            repository.postsOnSearch(searchString = searchString)
        }
    }
}
