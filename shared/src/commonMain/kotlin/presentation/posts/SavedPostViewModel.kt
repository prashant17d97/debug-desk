package presentation.posts

import core.datastore.repo.DataStoreRepository
import datamodel.model.PostModel
import datamodel.sealed.SaveOrRemoveProcessor
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import main.MainViewModel
import repo.Repository

/**
 * ViewModel responsible for handling saved posts functionality.
 *
 * This ViewModel manages the retrieval and saving/removing of posts from the data store repository.
 *
 * @param repository The repository providing access to the data store.
 * @param dataStoreRepository The repository for interacting with the data store.
 *
 * @author Prashant Singh
 */
class SavedPostViewModel(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository,
) : MainViewModel() {
    // StateFlow of saved posts retrieved from the data store repository
    val posts: StateFlow<List<PostModel>> = dataStoreRepository.posts

    /**
     * Retrieves saved posts from the data store repository.
     */
    fun getSavedPost() {
        viewModelScope.launch {
            dataStoreRepository.getPost()
        }
    }

    /**
     * Saves or removes a post based on the provided SaveOrRemoveProcessor.
     *
     * @param saveOrRemoveProcessor The processor indicating whether to save or remove the post.
     */
    fun saveOrRemove(saveOrRemoveProcessor: SaveOrRemoveProcessor) {
        viewModelScope.launch {
            when (saveOrRemoveProcessor) {
                is SaveOrRemoveProcessor.Remove ->
                    dataStoreRepository.removePost(saveOrRemoveProcessor.postModel)

                is SaveOrRemoveProcessor.Save -> {
                    // Do nothing for now
                    // dataStoreRepository.savePost(saveOrRemoveProcessor.postModel)
                }
            }
        }
    }
}
