package com.example.tagihin.data.remote.repository

import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.model.BillRequest
import com.example.tagihin.data.remote.model.BillResponse
import com.example.tagihin.data.remote.model.BillStatResponse
import com.example.tagihin.utils.PreferencesHelper
import io.reactivex.Maybe
import retrofit2.Response

class HomeRepository (val apiService : ApiService, val pref : PreferencesHelper){

    fun getPaidBill(page : Int, size : Int) : Maybe<Response<BillResponse>> {
        return apiService.getPaidBill(
            pref.getUsername()!!,3,page,size
        )
    }

    fun getUnpaidBill(page : Int, size : Int) : Maybe<Response<BillResponse>> {
        return apiService.getUnpaidBill(
            pref.getUsername()!!,3,page,size
        )
    }

    fun getPendingBill(page : Int, size : Int) : Maybe<Response<BillResponse>> {
        return apiService.getPendingBill(
            pref.getUsername()!!,3,page,size
        )
    }

    fun getBillStat() : Maybe<Response<BillStatResponse>> {
        return apiService.getBillStat(
            pref.getUsername()!!,3
        )
    }
}