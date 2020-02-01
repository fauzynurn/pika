package com.example.tagihin.di

import com.example.tagihin.view.bill.paid.PaidViewModel
import com.example.tagihin.view.bill.pending.PendingViewModel
import com.example.tagihin.view.bill.unpaid.UnpaidViewModel
import com.example.tagihin.view.detail.BillViewModel
import com.example.tagihin.view.home.HomeViewModel
import com.example.tagihin.view.login.LoginViewModel
import com.example.tagihin.view.bill.savedbill.SavedBillViewModel
import com.example.tagihin.view.officer.OfficerListViewModel
import com.example.tagihin.view.searchdil.SearchDilViewModel
import com.example.tagihin.view.settings.SettingViewModel
import com.example.tagihin.view.workorder.WorkOrderViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get() ) }
    viewModel { PaidViewModel(get() ) }
    viewModel { PendingViewModel(get() ) }
    viewModel { UnpaidViewModel(get() ) }
    viewModel { WorkOrderViewModel(get(),get()) }
    viewModel { SavedBillViewModel(get() ) }
    viewModel { HomeViewModel(get()) }
    viewModel{ BillViewModel(get()) }
    viewModel { SettingViewModel(get()) }
    viewModel { SearchDilViewModel(get()) }
    viewModel {OfficerListViewModel(get())}
}