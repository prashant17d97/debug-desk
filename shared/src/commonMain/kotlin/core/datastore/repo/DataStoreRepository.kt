package core.datastore.repo

import clickevents.AppTheme
import datamodel.model.PostModel
import kotlinx.coroutines.flow.StateFlow

/**
 * Represents a repository interface for managing data stored within the application, including posts,
 * application theme, and system palette preference.
 *
 * This interface provides access to various data flows such as posts, application theme, and system palette preference.
 * It also defines functions to save and retrieve posts, update the application theme, and toggle the use of the system palette.
 *
 * @property posts A [StateFlow] representing the list of posts stored within the application.
 * @property appTheme A [StateFlow] representing the current theme of the application.
 * @property useSystemPalette A [StateFlow] representing whether the application is configured to use the system palette.
 *
 * @see PostModel
 * @see AppTheme
 * @see StateFlow
 *
 * @constructor Creates an instance of [DataStoreRepository].
 *
 * @author Prashant Singh
 */
interface DataStoreRepository {
    val posts: StateFlow<List<PostModel>>
    val appTheme: StateFlow<AppTheme>
    val useSystemPalette: StateFlow<Boolean>

    /**
     * Saves a post to the data store.
     *
     * @param data The post data to be saved.
     */
    suspend fun savePost(data: PostModel)

    /**
     * Removes a post from the data store.
     *
     * @param data The post data to be removed.
     */
    suspend fun removePost(data: PostModel)

    /**
     * Saves the application theme to the data store.
     *
     * @param appTheme The theme to be saved.
     */
    suspend fun saveTheme(appTheme: AppTheme)

    /**
     * Toggles the use of the system palette within the application.
     *
     * @param useSystemPalette Indicates whether to use the system palette.
     */
    suspend fun saveSystemPalette(useSystemPalette:Boolean)

    /**
     * Retrieves the system palette preference from the data store.
     */
    suspend fun getSystemPalette()

    /**
     * Retrieves posts from the data store.
     */
    suspend fun getPost()

    /**
     * Retrieves the application theme from the data store.
     */
    suspend fun getTheme()

}

