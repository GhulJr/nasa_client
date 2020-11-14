package com.ghuljr.nasaclient

import android.app.Application
import com.ghuljr.nasaclient.di.networkModule
import com.ghuljr.nasaclient.di.presenterModule
import com.ghuljr.nasaclient.di.repositoryModule
import com.ghuljr.nasaclient.di.storageModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(
                listOf(
                    networkModule,
                    storageModule,
                    presenterModule,
                    repositoryModule
                )
            )
        }
    }
}