package com.example.tagihin.di

import com.example.tagihin.view.detail.BillViewModel
import com.example.tagihin.view.home.HomeViewModel
import com.example.tagihin.view.login.LoginViewModel
import com.example.tagihin.view.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get() ) }
    viewModel { HomeViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel{ BillViewModel(get()) }
}