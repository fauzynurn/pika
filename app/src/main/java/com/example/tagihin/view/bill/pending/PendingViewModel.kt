package com.example.tagihin.view.bill.pending

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.tagihin.data.remote.repository.PendingRepository
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PendingViewModel (val repo : PendingRepository) : ViewModel() {
    var shouldTriggerSomething: MutableLiveData<String> = MutableLiveData()
    var query : MutableLiveData<String> = MutableLiveData()
    //work order list size live data, for hiding or showing bottom selection
    var woListSize : MutableLiveData<Int> = MutableLiveData()
    //save the latest work order list which then sends it to all the subscribers (fragments)
    var woList : MutableLiveData<ArrayList<Int>> = MutableLiveData()
    //notify all of the subcriber to switch their multi-select mode on or off
    var multiSelectMode : MutableLiveData<Boolean> = MutableLiveData()
    //notify all of the subscriber to reload all of the view after resetting selection
    var resetSelection : MutableLiveData<Boolean> = MutableLiveData()
    var disposable : Disposable? = null
    var updateWo : MutableLiveData<Boolean> = MutableLiveData()
    var transferBill : MutableLiveData<Boolean> = MutableLiveData()
    private val repoResult = Transformations.map(shouldTriggerSomething) {
        repo.getPendingBill()
    }
    private val localSearch = Transformations.map(query) {
        repo.searchPendingBill(it)
    }
    val searchResult = Transformations.switchMap(localSearch){
        it.pagedList
    }
    val posts = Transformations.switchMap(repoResult) {
        it.pagedList
    }
    val networkState = Transformations.switchMap(repoResult) { it.networkState }
    val refreshState = Transformations.switchMap(repoResult) { it.refreshState }

    fun refresh() {
        repoResult.value?.refresh?.invoke()
        localSearch.value?.refresh?.invoke()
    }

    fun retry() {
        val listing = repoResult.value
        listing?.retry?.invoke()
    }

    fun moveToWO(list : ArrayList<Int>){
        val hashMap : HashMap<String,Int> = HashMap()
        for((index, value) in list.withIndex()){
            hashMap[String.format("checked_id[%d]", index)] = value
        }
        disposable = repo.moveToWO(hashMap)
            .subscribeOn(Schedulers.io())
            .subscribe({
                updateWo.postValue(it.body()?.status)
            }, {
                Timber.e(it)
            })
    }

    fun transferBill(username : String, list : ArrayList<Int>){
        val hashMap : HashMap<String,Int> = HashMap()
        for((index, value) in list.withIndex()){
            hashMap[String.format("checked_id[%d]", index)] = value
        }
        disposable = repo.transferBill(username,hashMap)
            .subscribeOn(Schedulers.io())
            .subscribe({
                transferBill.postValue(it.body()?.status)
            }, {
                Timber.e(it)
            })
    }
}