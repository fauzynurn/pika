package com.example.tagihin

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.example.tagihin.data.local.TagihinDb
import com.example.tagihin.di.*
import com.example.tagihin.utils.receiver.ConnectivityChangeReceiver
import com.example.tagihin.view.detail.BillViewModel
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class TagihinApp : Application() {
    val db : TagihinDb by inject()
    val viewModel : BillViewModel by inject()
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
        setInstance(this)
        startKoin {
            androidContext(this@TagihinApp)
            modules(
                listOf(
                    viewModelModule, repositoryModule, prefModule, remoteDataSourceModule,
                    ruleSource
                )
            )
        }
//        val filter = IntentFilter()
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
//        registerReceiver(ConnectivityChangeReceiver(), filter)
    }
}