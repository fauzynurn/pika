package com.example.tagihin.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tagihin.data.remote.model.Bill
import com.example.tagihin.data.remote.model.BillStat
import com.example.tagihin.data.remote.model.GeneralResponse
import com.example.tagihin.data.remote.repository.HomeRepository
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel (val repo : HomeRepository) : ViewModel(){
    var paidBill: MutableLiveData<List<Bill>> = MutableLiveData()
    var unpaidBill: MutableLiveData<List<Bill>> = MutableLiveData()
    var pendingBill: MutableLiveData<List<Bill>> = MutableLiveData()
    var pendingWorkOrder: MutableLiveData<List<Bill>> = MutableLiveData()
    var unpaidWorkOrder: MutableLiveData<List<Bill>> = MutableLiveData()
    var billStat: MutableLiveData<BillStat> = MutableLiveData()
    var error : MutableLiveData<String> = MutableLiveData()
    var reloadLiveData : MutableLiveData<Boolean> = MutableLiveData()
    var reloadWorkOrderData : MutableLiveData<Boolean> = MutableLiveData()
    var updateWo : MutableLiveData<Boolean> = MutableLiveData()
    var woListData : MutableLiveData<Int> = MutableLiveData()

    var multiSelectState : MutableLiveData<Boolean> = MutableLiveData()
    private var disposable : Disposable? = null
    fun getPaidBill(page : Int, size : Int){
        disposable = repo.getPaidBill(page,size)
            .subscribeOn(Schedulers.io())
            .subscribe({
                paidBill.postValue(it.body()?.data)
            }, {
                error.postValue(it.message)
            })
    }

    fun getUnpaidBill(page : Int, size : Int){
        disposable = repo.getUnpaidBill(page,size)
            .subscribeOn(Schedulers.io())
            .subscribe({
                unpaidBill.postValue(it.body()?.data)
            }, {
                error.postValue(it.message)
            })
    }

    fun getPendingBill(page : Int, size : Int){
        disposable = repo.getPendingBill(page,size)
            .subscribeOn(Schedulers.io())
            .subscribe({

                pendingBill.postValue(
                    it.body()?.data
                )
            }, {
                error.postValue(it.message)
            })
    }

    fun getPendingWorkOrderBill(page : Int, size : Int){
        disposable = repo.getPendingWorkOrderBill(page,size)
            .subscribeOn(Schedulers.io())
            .subscribe({
                pendingWorkOrder.postValue(it.body()?.data)
            }, {
                error.postValue(it.message)
            })
    }

    fun getUnpaidWorkOrderBill(page : Int, size : Int){
        disposable = repo.getUnpaidWorkOrderBill(page,size)
            .subscribeOn(Schedulers.io())
            .subscribe({
                unpaidWorkOrder.postValue(it.body()?.data)
            }, {
                error.postValue(it.message)
            })
    }

    fun getBillStat(){
        disposable = repo.getBillStat()
            .subscribeOn(Schedulers.io())
            .subscribe({
                billStat.postValue(it.body()?.data)
            }, {
                error.postValue(it.message)
            })
//        billStat.postValue(BillStat(
//            12,11,0,1
//        ))
    }

    fun moveToWO(list : ArrayList<String>){
        disposable = repo.moveToWO(list)
            .subscribeOn(Schedulers.io())
            .subscribe({
                updateWo.postValue(it.body()?.status)
            }, {
                error.postValue(it.message)
            })
    }
}