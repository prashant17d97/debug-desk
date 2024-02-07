package core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import core.datastore.DataStoreObj.createDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

/**
 * Creates a DataStore instance for managing preferences data on the iOS side.
 * @return A DataStore instance for managing preferences.
 * @OptIn Marks the function as experimental, allowing the use of foreign APIs.
 * @param producePath A lambda function that produces the path for storing the data store file.
 *
 * @author Prashant Singh
 */
@OptIn(ExperimentalForeignApi::class)
fun dataStore(): DataStore<Preferences> =
    createDataStore(
        producePath = {
            // Produce the path for storing the data store file...
            val documentDirectory: NSURL? =
                NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null,
                )
            requireNotNull(documentDirectory).path + "/${DataStoreObj.data_store_name}"
        },
    )

