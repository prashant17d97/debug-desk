package core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import core.datastore.DataStoreObj.data_store_name
import okio.Path.Companion.toPath


/**
 * Object responsible for creating a DataStore instance with the specified name and path.
 * It provides a function to create a DataStore with a custom path using the PreferenceDataStoreFactory.
 *
 * @property data_store_name The name of the DataStore file.
 *
 * @see DataStore
 * @see Preferences
 * @see PreferenceDataStoreFactory
 *
 * @param producePath A function that produces the path where the DataStore file will be created.
 *
 * @constructor Creates an instance of [DataStoreObj].
 *
 * @author Prashant Singh
 */
object DataStoreObj {
    const val data_store_name = "DebugDeskDataStore.preferences_pb"

    /**
     * Creates a DataStore instance with a custom path using the PreferenceDataStoreFactory.
     *
     * @param producePath A function that produces the path where the DataStore file will be created.
     * @return The created DataStore instance.
     */
    fun createDataStore(producePath: () -> String): DataStore<Preferences> =
        PreferenceDataStoreFactory.createWithPath(
            corruptionHandler = null,
            migrations = emptyList(),
            produceFile = { producePath().toPath() },
        )
}

