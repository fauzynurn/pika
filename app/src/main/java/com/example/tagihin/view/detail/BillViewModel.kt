package com.example.tagihin.view.detail

import android.widget.LinearLayout
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tagihin.data.local.TagihinDb
import com.example.tagihin.data.remote.model.BillStat
import com.example.tagihin.data.remote.model.GeneralResponse
import com.example.tagihin.data.remote.model.TempBill
import com.example.tagihin.data.remote.repository.BillRepository
import com.example.tagihin.data.remote.repository.HomeRepository
import com.example.tagihin.utils.Consts
import com.example.tagihin.utils.Consts.Companion.OPTIONS
import com.example.tagihin.utils.DateUtils
import com.weiwangcn.betterspinner.library.BetterSpinner
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class BillViewModel(val repo: BillRepository) : ViewModel() {
    var updateBill: MutableLiveData<Boolean> = MutableLiveData()
    var localUpdateBill: MutableLiveData<Boolean> = MutableLiveData()
    var error: MutableLiveData<String> = MutableLiveData()
    //date should be in format DD/MM/YYYY
    var status: MutableLiveData<String> = MutableLiveData("PENDING")
    var dateChange: MutableLiveData<String> = MutableLiveData()
    var note: String = ""
    private var disposable: Disposable? = null

    fun updateBill(
        billId: Int,
        name: String,
        orderCode: String,
        statusBefore : String
    ) {
//        Timber.i("DATE: %s",dateChange)
//        Timber.i("status: %s",status)
//        Timber.i("note: %s",note)
        //should be handled using rx with disabled button
        val date = if (status.value!! == Consts.PAID) {
            DateUtils.getCurrentDate("yyyy-MM-dd")
        } else {
            dateChange.value
        }
        repo.updateBill(
            statusBefore,
            billId,
            status.value!!,
            date!!,
            note,
            status.value!!,
            name,
            orderCode,
            {
                updateBill.postValue(it)
            },
            {
                error.postValue(it)
            }, {
                localUpdateBill.postValue(it)
            }
        )
    }
}
