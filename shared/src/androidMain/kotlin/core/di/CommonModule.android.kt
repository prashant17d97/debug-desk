package core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import core.datastore.dataStore
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Defines the Koin module for Android platform-specific dependencies.
 *
 * This module provides Android-specific implementations for platform-related dependencies.
 */
actual val platformModule: Module = module {
    // Provides a string value indicating the platform as "Android"
    single(named("Platform")) { "Android" }

    // Provides a DataStore object for storing preferences in Android
    single<DataStore<Preferences>> {
        // Calls the dataStore function to create a DataStore object using the application context
        dataStore(get())
    }
}

