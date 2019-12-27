package com.example.tagihin.view.searchdil

import android.annotation.SuppressLint
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.tagihin.data.remote.model.DilItemRequest
import com.example.tagihin.data.remote.model.DilItemResponse
import com.example.tagihin.data.remote.model.DilItemValidationRequest
import com.example.tagihin.data.remote.repository.SearchDilRepository
import com.example.tagihin.utils.SingleLiveEvent
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SearchDilViewModel (val repo : SearchDilRepository) : ViewModel() {
    val error = MutableLiveData<String>()
    var dilItem = MutableLiveData<DilItemResponse?>(DilItemResponse())
    var query = MutableLiveData<String>()
    var loadingState = MutableLiveData<Boolean>(false)
    var updateSuccess = SingleLiveEvent<Boolean>()
    lateinit var dilValidate : DilItemValidationRequest
    var dilItemRequest : LiveData<DilItemRequest?> = Transformations.map(dilItem) {
        DilItemRequest(
            it?.id.throwEmptyStringIfNull(),
            it?.idpel.throwEmptyStringIfNull(),
            it?.nama.throwEmptyStringIfNull(),
            it?.alamat.throwEmptyStringIfNull(),
            it?.tarif.throwEmptyStringIfNull(),
            it?.daya.throwEmptyStringIfNull(),
            it?.gardu.throwEmptyStringIfNull(),
            it?.merek_kwh.throwEmptyStringIfNull(),
            it?.thtera_kwh.throwEmptyStringIfNull(),
            it?.nomor_kwh.throwEmptyStringIfNull(),
            it?.kor_x.throwEmptyStringIfNull(),
            it?.kor_y.throwEmptyStringIfNull(),
            it?.no_hp.throwEmptyStringIfNull(),
            it?.tanggal.throwEmptyStringIfNull(),
            it?.meter_rusak.throwEmptyStringIfNull(),
            it?.meter_siaga.throwEmptyStringIfNull(),
            it?.pasang_siaga.throwEmptyStringIfNull()
            )
    }

    @SuppressLint("CheckResult")
    fun searchDil(queryy : String){
        repo.searchDil(queryy)
            .subscribeOn(Schedulers.io())
            .subscribe({
                loadingState.postValue(false)
                dilItem.postValue(it?.body()?.data)
                dilValidate = DilItemValidationRequest(id = it.body()?.data?.id!!)
            }, {
                error.postValue(it.message!!)
            })
    }

    @SuppressLint("CheckResult")
    fun sendConfirmationForm(){
        repo.uploadConfirmation(
            dilItemRequest.value?.id.throwEmptyStringIfNull(),
            dilItemRequest.value?.idpel.throwEmptyStringIfNull(),
            dilItemRequest.value?.nama.throwEmptyStringIfNull(),
            dilItemRequest.value?.alamat.throwEmptyStringIfNull(),
            dilItemRequest.value?.tarif.throwEmptyStringIfNull(),
            dilItemRequest.value?.daya.throwEmptyStringIfNull(),
            dilItemRequest.value?.tanggal.throwEmptyStringIfNull(),
            dilItemRequest.value?.meter_rusak.throwEmptyStringIfNull(),
            dilItemRequest.value?.meter_siaga.throwEmptyStringIfNull(),
            dilItemRequest.value?.pasang_siaga.throwEmptyStringIfNull(),
            dilItemRequest.value?.no_hp.throwEmptyStringIfNull(),
            dilItemRequest.value?.kor_x.throwEmptyStringIfNull(),
            dilItemRequest.value?.kor_y.throwEmptyStringIfNull(),
            dilItemRequest.value?.foto_siaga!!,
            dilItemRequest.value?.foto_rusak!!,
            dilItemRequest.value?.foto_bangunan!!
        )
            .subscribeOn(Schedulers.io())
            .subscribe({
                updateSuccess.postValue(true)
            }, {
                error.postValue(it.message!!)
            })
    }

    fun String?.throwEmptyStringIfNull() : String{
        return if(this != "" && this != null) this else ""
    }

    @SuppressLint("CheckResult")
    fun sendValidationForm() {
        repo.validateDil(dilValidate)
            .subscribeOn(Schedulers.io())
            .subscribe({
                updateSuccess.postValue(true)
            }, {
                error.postValue(it.message!!)
            })
    }
}