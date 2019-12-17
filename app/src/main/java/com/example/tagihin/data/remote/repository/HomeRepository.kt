package com.example.tagihin.data.remote.repository

import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.model.*
import com.example.tagihin.utils.PreferencesHelper
import io.reactivex.Maybe
import retrofit2.Response

//try with inMemoryDB first
class HomeRepository(
    val apiService: ApiService,
    val pref: PreferencesHelper
) {
    fun getBillStat(): Maybe<Response<BillStatResponse>> {
        return apiService.getBillStat(
            pref.getUsername()!!, 3
        )
    }
}
//    fun getPaidBill(page : Int, size : Int) : Maybe<Response<BillResponse>> {
//        return apiService.getPaidBill(
//            pref.getUsername()!!,3,page,size
//        )
//    }
//
//    fun getUnpaidBill(page : Int, size : Int) : Maybe<Response<BillResponse>> {
//        return apiService.getUnpaidBill(
//            pref.getUsername()!!,3,page,size
//        )
//    }
//
//    fun getPendingBill(page : Int, size : Int) : Maybe<Response<BillResponse>> {
//        return apiService.getPendingBill(
//            pref.getUsername()!!,3,page,size
//        )
//    }
//
//    fun getPendingWorkOrderBill(page : Int, size : Int) : Maybe<Response<BillResponse>> {
//        return apiService.getPendingWorkOrderBill(
//            pref.getUsername()!!,3,page,size
//        )
//    }
//
//    fun getUnpaidWorkOrderBill(page : Int, size : Int) : Maybe<Response<BillResponse>> {
//        return apiService.getUnpaidWorkOrderBill(
//            pref.getUsername()!!,3,page,size
//        )
//    }
//
//
//    fun moveToWO(list : HashMap<String,Int>) : Maybe<Response<GeneralResponse>> {
//        return apiService.moveToWO(
//            pref.getUsername()!!,3,list
//        )
//    }