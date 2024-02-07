package core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Wrapper class for managing data stored within a DataStore instance. It provides functions to
 * retrieve, set, and remove data of various types from the DataStore, as well as checking for the
 * existence of a key and clearing the entire DataStore.
 *
 * @property dataStore The DataStore instance used for data storage.
 *
 * @param dataStore The DataStore instance used for data storage.
 *
 * @constructor Creates an instance of [DataStore] with the provided DataStore instance.
 *
 * @author Prashant Singh
 */
class DataStore(private val dataStore: DataStore<Preferences>) {
    /**
     * Retrieves a boolean value associated with the specified key from the DataStore.
     *
     * @param key The key used to retrieve the boolean value.
     * @param defaultValue The default value to return if the key is not found.
     * @return A [Flow] emitting the retrieved boolean value.
     */
    fun getBoolean(
        key: Preferences.Key<Boolean>,
        defaultValue: Boolean = false,
    ): Flow<Boolean> {
        return dataStore.data.map { preferences -> preferences[key] ?: defaultValue }
    }

    /**
     * Retrieves a string value associated with the specified key from the DataStore.
     *
     * @param key The key used to retrieve the string value.
     * @param defaultValue The default value to return if the key is not found.
     * @return A [Flow] emitting the retrieved string value.
     */
    fun getString(
        key: Preferences.Key<String>,
        defaultValue: String = "",
    ): Flow<String> {
        return dataStore.data.map { preferences -> preferences[key] ?: defaultValue }
    }

    /**
     * Retrieves an integer value associated with the specified key from the DataStore.
     *
     * @param key The key used to retrieve the integer value.
     * @return A [Flow] emitting the retrieved integer value.
     */
    fun getInt(key: Preferences.Key<Int>): Flow<Int> {
        return dataStore.data.map { preferences -> preferences[key] ?: 0 }
    }

    /**
     * Sets a value associated with the specified key in the DataStore.
     *
     * @param key The key used to set the value.
     * @param value The value to be set.
     */
    suspend fun <T> set(
        key: Preferences.Key<T>,
        value: T,
    ) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    /**
     * Removes the value associated with the specified key from the DataStore.
     *
     * @param key The key used to remove the value.
     */
    suspend fun <T> remove(key: Preferences.Key<T>) {
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }

    /**
     * Checks whether the DataStore contains a value associated with the specified key.
     *
     * @param key The key to check for existence.
     * @return A [Flow] emitting a boolean value indicating whether the key exists in the DataStore.
     */
    fun <T> containsKey(key: Preferences.Key<T>): Flow<Boolean> {
        return dataStore.data.map { preference ->
            preference.contains(key)
        }
    }

    /**
     * Clears all data stored in the DataStore.
     */
    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
