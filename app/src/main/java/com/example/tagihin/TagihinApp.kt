package com.example.tagihin

import android.app.Application
import com.example.tagihin.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class TagihinApp : Application() {

    companion object {
        lateinit var sInstance: TagihinApp
        fun getAppContext(): TagihinApp {
            return sInstance
        }

        @Synchronized
        private fun setInstance(app: TagihinApp) {
            sInstance = app
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            androidContext(this@TagihinApp)
            modules(
                listOf(
                    viewModelModule, repositoryModule, prefModule, remoteDataSourceModule,
                    ruleSource
                )
            )
        }
    }
}