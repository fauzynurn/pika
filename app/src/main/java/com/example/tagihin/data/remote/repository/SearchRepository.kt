package com.example.tagihin.data.remote.repository

import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.model.BillResponse
import com.example.tagihin.utils.PreferencesHelper
import io.reactivex.Maybe

class SearchRepository (val apiService : ApiService, val pref : PreferencesHelper){
    fun search(query : String) : Maybe<retrofit2.Response<BillResponse>> {
        return apiService.search(
            pref.getUsername()!!,query
        )
    }
}