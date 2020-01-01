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

class SearchDilViewModel(val repo: SearchDilRepository) : ViewModel() {
    val error = MutableLiveData<String>()
    var dilItem = MutableLiveData<DilItemResponse?>(DilItemResponse())
    var query = MutableLiveData<String>()
    var loadingState = MutableLiveData<Boolean>(false)
    var cabutSiaga = MutableLiveData<String>(0.toString())
    var cost = MutableLiveData(0)
    var updateSuccess = SingleLiveEvent<Boolean>()
    var dilValidate = MutableLiveData<DilItemValidationRequest>(DilItemValidationRequest())
    var dilItemRequest: LiveData<DilItemRequest?> = Transformations.map(dilItem) {
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
            it?.pasang_siaga.throwZeroIfNull(),
            it?.x_upload.throwZeroIfNull(),
            it?.y_upload.throwZeroIfNull()
        )
    }
    var latLongFinal = MutableLiveData<Pair<Double, Double>>()
    var latLong: LiveData<Pair<Double, Double>> =
        Transformations.map(dilItemRequest) {
            Pair(it?.x_upload?.toDouble()!!, it.y_upload.toDouble())
        }


    @SuppressLint("CheckResult")
    fun searchDil(queryy: String) {
        repo.searchDil(queryy)
            .subscribeOn(Schedulers.io())
            .subscribe({
                loadingState.postValue(false)
                dilItem.postValue(it?.body()?.data)
                dilValidate.value?.id =  it.body()?.data?.id!!.toInt()
                cabutSiaga.postValue(it?.body()?.data?.pasang_siaga!!.toString())
            }, {
                error.postValue(it.message!!)
            })
    }

    @SuppressLint("CheckResult")
    fun sendConfirmationForm() {
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
            dilItemRequest.value?.pasang_siaga.toString(),
            dilItemRequest.value?.no_hp.throwEmptyStringIfNull(),
            latLongFinal.value?.first.toString(),
            latLongFinal.value?.second.toString(),
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

    fun String?.throwEmptyStringIfNull(): String {
        return if (this != "" && this != null) this else ""
    }

    fun String?.throwZeroIfNull(): String {
        return if (this != "" && this != null) this else "0"
    }

    fun Int?.throwZeroIfNull(): Int {
        return this ?: 0
    }

    fun calculateKwhUsed(cabutSiaga: Int, pasangSiaga: Int) : Int = cabutSiaga.minus(pasangSiaga)

    fun calculateBill(kwhUsed : Int, cost : Int) : Int = (kwhUsed * cost * 0.1).toInt()

    @SuppressLint("CheckResult")
    fun sendValidationForm() {
        dilValidate.value?.tarif = cost.value!!
        dilValidate.value?.cabut_siaga = cabutSiaga.value?.toInt()!!
        repo.validateDil(dilValidate.value!!)
            .subscribeOn(Schedulers.io())
            .subscribe({
                updateSuccess.postValue(true)
            }, {
                error.postValue(it.message!!)
            })
    }
}