package org.debugdesk.app

import android.app.Application
import android.content.Context
import core.di.initiateKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class DebugDesk : Application() {
    override fun onCreate() {
        super.onCreate()
        initiateKoin(
            listOf(
                module {
                    single<Context> { this@DebugDesk }
                }
            )
        )
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}