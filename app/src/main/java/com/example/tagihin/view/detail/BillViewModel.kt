package com.example.tagihin.view.detail

import android.widget.LinearLayout
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tagihin.data.remote.model.Bill
import com.example.tagihin.data.remote.model.BillStat
import com.example.tagihin.data.remote.model.GeneralResponse
import com.example.tagihin.data.remote.repository.BillRepository
import com.example.tagihin.data.remote.repository.HomeRepository
import com.example.tagihin.utils.Consts
import com.example.tagihin.utils.Consts.Companion.OPTIONS
import com.example.tagihin.utils.DateUtils
import com.weiwangcn.betterspinner.library.BetterSpinner
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class BillViewModel (val repo : BillRepository) : ViewModel(){
    var updateBill: MutableLiveData<Boolean> = MutableLiveData()
    var error : MutableLiveData<String> = MutableLiveData()
    //date should be in format DD/MM/YYYY
    var status : MutableLiveData<String> = MutableLiveData("PENDING")
    var dateChange : MutableLiveData<String> = MutableLiveData()
    var note : String = ""
    private var disposable : Disposable? = null

    fun updateBill(billId : Int){
//        Timber.i("DATE: %s",dateChange)
//        Timber.i("status: %s",status)
//        Timber.i("note: %s",note)
        //should be handled using rx with disabled button
        val date = if(status.value!! == Consts.PAID){
            DateUtils.getCurrentDate("dd/MM/yyyy")
        }else{
            dateChange.value
        }
        disposable = repo.updateBill(billId, status.value!! , date!! , note )
            .subscribeOn(Schedulers.io())
            .subscribe({
                updateBill.postValue(it.body()?.status!!)
            }, {
                error.postValue(it.message)
            })
    }

}
