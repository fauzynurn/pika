package com.example.tagihin.view.bill.savedbill

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.tagihin.data.remote.model.SavedBillRequestItem
import com.example.tagihin.data.remote.model.TempBill
import com.example.tagihin.data.remote.repository.PendingRepository
import com.example.tagihin.data.remote.repository.PendingWorkOrderRepository
import com.example.tagihin.data.remote.repository.SavedBillRepository

class SavedBillViewModel (val repo : SavedBillRepository) : ViewModel() {
    var shouldTriggerSomething: MutableLiveData<String> = MutableLiveData()
    var onSendSuccess = MutableLiveData<Boolean>()
    var onSendFail = MutableLiveData<String>()

    private val savedPending = Transformations.map(shouldTriggerSomething) {
        repo.getSavedPendingBill(10)
    }
    private val savedUnpaid = Transformations.map(shouldTriggerSomething) {
        repo.getSavedUnpaidBill(10)
    }

    val _savedPending = Transformations.switchMap(savedPending){
        it
    }

    val _savedUnpaid = Transformations.switchMap(savedUnpaid){
        it
    }

    fun transform(list1 : MutableList<TempBill>,
                  list2 : MutableList<TempBill>) : ArrayList<SavedBillRequestItem>{
        val convertedPending = list1.map {
            SavedBillRequestItem(
                id = it.id,
                status1 = it.status1,
                tanggal = it.date,
                note = it.note
            )
        }.toMutableList()
        val convertedUnpaid = list2.map {
            SavedBillRequestItem(
                id = it.id,
                status1 = it.status1,
                tanggal = it.date,
                note = it.note
            )
        }
        convertedPending.addAll(convertedUnpaid)
        return convertedPending as ArrayList<SavedBillRequestItem>
    }
    fun sendRequest(){
        repo.getAllSavedRequest(
            {
                repo.sendRequest(it,{
                    onSendSuccess.postValue(it)
                    repo.deleteAllSavedRecord()
                },{
                    onSendFail.postValue(it)
                })
            },this@SavedBillViewModel::transform
        )
    }
}