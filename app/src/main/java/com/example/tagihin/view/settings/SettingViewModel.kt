package com.example.tagihin.view.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tagihin.data.remote.repository.SettingRepository

class SettingViewModel (val repo : SettingRepository) : ViewModel() {
    val reset = MutableLiveData<Boolean>()

    fun resetWo(){
        repo.resetWo {
            reset.postValue(it)
        }
        repo.resetLocalWo()
    }

    fun resetBill(){
        repo.resetBill {
            reset.postValue(it)
        }
        repo.resetLocalBill()
    }
}