package com.example.tagihin.di

import com.example.tagihin.TagihinApp
import com.example.tagihin.data.local.TagihinDb
import com.example.tagihin.data.remote.repository.*
import com.example.tagihin.utils.PreferencesHelper
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.concurrent.Executors

val repositoryModule = module {
    single { PendingWorkOrderRepository(get(),get(),get(),Executors.newSingleThreadExecutor(),10) }
    single { PaidRepository(get(),get(),get(),Executors.newSingleThreadExecutor(),10) }
    single { UnpaidWorkOrderRepository(get(),get(),get(),Executors.newSingleThreadExecutor(),10) }
    single { PendingRepository(get(),get(),get(),Executors.newSingleThreadExecutor(),10) }
    single { UnpaidRepository(get(),get(),get(),Executors.newSingleThreadExecutor(),10) }
    single { HomeRepository(get(), get())}
    single { BillRepository(Executors.newSingleThreadExecutor(),androidContext(), get(), get(), get()) }
    single { LoginRepository(get()) }
    single {SavedBillRepository(get(),get(),Executors.newSingleThreadExecutor(), get())}
    single{ TagihinDb.create(androidContext(), false)}
    single { SettingRepository(Executors.newSingleThreadExecutor(),get(), get(), get())}
}

val prefModule = module {
    single { PreferencesHelper(androidApplication()) }
}