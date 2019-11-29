package com.example.tagihin.data.remote.repository

import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.model.LoginRequest
import com.example.tagihin.data.remote.model.LoginResponse
import com.example.tagihin.data.remote.model.SampleResponse
import com.example.tagihin.utils.PreferencesHelper
import io.reactivex.Maybe
import retrofit2.Response

class LoginRepository (val apiService : ApiService){
    fun login(username : String, password : String): Maybe<Response<LoginResponse>> {
        return apiService.login(
            username,password
        )
    }
//    fun todos(): Maybe<Response<SampleResponse>> {
//        return apiService.todos()
//    }
}