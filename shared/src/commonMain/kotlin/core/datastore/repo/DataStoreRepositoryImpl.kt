package core.datastore.repo

import androidx.datastore.preferences.core.stringPreferencesKey
import clickevents.AppTheme
import core.datastore.DataStore
import core.log.Logcat
import datamodel.model.PostModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import utils.CommonFunctions.parseData
import utils.CommonFunctions.parseJson

/**
 * Implements the [DataStoreRepository] interface to manage data stored within the application using DataStore.
 * It handles operations such as saving and retrieving posts, updating the application theme, and managing
 * the system palette preference.
 *
 * @param dataStore The DataStore instance used for data storage.
 *
 * @constructor Creates an instance of [DataStoreRepositoryImpl] with the provided DataStore instance.
 *
 * @author Prashant Singh
 */
class DataStoreRepositoryImpl(private val dataStore: DataStore) : DataStoreRepository {

    private val dataStorePost = dataStore.getString(SAVED_POSTS)
    private val dataStoreAppTheme = dataStore.getString(APP_THEME)
    private val dataStoreSystemPalette = dataStore.getString(USE_SYSTEM_PALETTE)

    private val _posts: MutableStateFlow<List<PostModel>> = MutableStateFlow(emptyList())
    override val posts: StateFlow<List<PostModel>> = _posts

    private val _appTheme: MutableStateFlow<AppTheme> = MutableStateFlow(AppTheme.DARK)
    override val appTheme: StateFlow<AppTheme> = _appTheme

    private val _useSystemPalette: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val useSystemPalette: StateFlow<Boolean> = _useSystemPalette

    /**
     * Saves a post to the data store.
     *
     * @param data The post data to be saved.
     */
    override suspend fun savePost(data: PostModel) {
        if (!getSavedPost().any { it._id == data._id }) {
            val parsedJson = parseData(getSavedPost() + listOf(data.copy(isSelected = true)))
            dataStore.set(SAVED_POSTS, parsedJson)
        }
    }

    /**
     * Retrieves posts from the data store.
     */
    override suspend fun getPost() {
        _posts.tryEmit(getSavedPost())
    }

    /**
     * Removes a post from the data store.
     *
     * @param data The post data to be removed.
     */
    override suspend fun removePost(data: PostModel) {
        val updatedPosts = getSavedPost().filter { it._id != data._id }
        Logcat.d("DataStoreRepositoryImpl", "updatedPosts: $updatedPosts")

        if (updatedPosts.isEmpty()) {
            dataStore.remove(SAVED_POSTS)
        } else {
            dataStore.set(SAVED_POSTS, parseData(updatedPosts))
        }
        getPost()
    }

    companion object {
        private val SAVED_POSTS = stringPreferencesKey("SAVED_POSTS")
        private val APP_THEME = stringPreferencesKey("APP_THEME")
        private val USE_SYSTEM_PALETTE = stringPreferencesKey("USE_SYSTEM_PALETTE")
    }

    private suspend fun getSavedPost(): List<PostModel> {
        return try {
            dataStorePost.first().parseJson<List<PostModel>>()
        } catch (ex: Exception) {
            emptyList()
        }
    }

    /**
     * Saves the application theme to the data store.
     *
     * @param appTheme The theme to be saved.
     */
    override suspend fun saveTheme(appTheme: AppTheme) {
        dataStore.set(APP_THEME, appTheme.name)
        getTheme()
    }

    /**
     * Toggles the use of the system palette within the application.
     *
     * @param useSystemPalette Indicates whether to use the system palette.
     */
    override suspend fun saveSystemPalette(useSystemPalette: Boolean) {
        dataStore.set(USE_SYSTEM_PALETTE, "$useSystemPalette")
        getSystemPalette()
    }

    /**
     * Retrieves the system palette preference from the data store.
     */
    override suspend fun getSystemPalette() {
        _useSystemPalette.tryEmit(dataStoreSystemPalette.first().toBoolean())
    }

    /**
     * Retrieves the application theme from the data store.
     */
    override suspend fun getTheme() {
        _appTheme.tryEmit(dataStoreAppTheme.first().toAppTheme())
    }

    /**
     * Converts a string representation of the theme to its corresponding [AppTheme] enum value.
     */
    private fun String.toAppTheme(): AppTheme {
        return when {
            this == AppTheme.DARK.name -> AppTheme.DARK
            this == AppTheme.LIGHT.name -> AppTheme.LIGHT
            this == AppTheme.SYSTEM_DEFAULT.name -> AppTheme.SYSTEM_DEFAULT
            else -> AppTheme.DARK
        }
    }
}
