package core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import core.datastore.dataStore
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Koin module for the iOS platform.
 * Defines dependencies specific to iOS.
 */
actual val platformModule: Module =
    module {
        // Provides a string indicating the platform as "iOS"
        single(named("Platform")) { "iOS" }

        // Provides a DataStore instance for managing preferences on the iOS side
        single<DataStore<Preferences>> { dataStore() }
    }
