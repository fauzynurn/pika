package com.example.tagihin.view.login

import android.widget.Toast
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tagihin.data.remote.model.LoginRequest
import com.example.tagihin.data.remote.model.LoginResponse
import com.example.tagihin.data.remote.model.SampleResponse
import com.example.tagihin.data.remote.repository.LoginRepository
import com.google.gson.Gson
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class LoginViewModel (val repo : LoginRepository) : ViewModel() {
    var username: String = ""
    var password: String = ""
    var loginRequest: LoginRequest? = null
    var disposable: Disposable? = null
    val login: MutableLiveData<LoginResponse> = MutableLiveData()
    val message: MutableLiveData<String> = MutableLiveData()
    fun login() {
        disposable = repo.login(username,password)
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it.isSuccessful) {
                    login.postValue(it.body())
                } else {
                    message.postValue(it.message())
                }
            }, {
                it.printStackTrace()
            })
    }
}