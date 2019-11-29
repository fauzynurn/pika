package com.example.tagihin.data.remote.repository

import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.model.GeneralResponse
import com.example.tagihin.utils.PreferencesHelper
import io.reactivex.Maybe
import retrofit2.Response

class BillRepository (val apiService : ApiService, val pref : PreferencesHelper){

    fun updateBill(billId : Int, status : String, date : String, note : String) : Maybe<Response<GeneralResponse>> {
        return apiService.updateBill(
            pref.getUsername()!!,billId, status,date,note
        )
    }
}