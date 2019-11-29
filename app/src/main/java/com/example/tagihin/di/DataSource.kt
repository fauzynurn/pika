package com.example.tagihin.di

import com.example.tagihin.data.remote.repository.BillRepository
import com.example.tagihin.data.remote.repository.HomeRepository
import com.example.tagihin.data.remote.repository.LoginRepository
import com.example.tagihin.data.remote.repository.SearchRepository
import com.example.tagihin.utils.PreferencesHelper
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repositoryModule = module {
    single { SearchRepository(get(), get()) }
    single { HomeRepository(get(), get()) }
    single { BillRepository(get(), get()) }
    single { LoginRepository(get()) }
}

val prefModule = module {
    single { PreferencesHelper(androidApplication()) }
}