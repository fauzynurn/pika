package com.example.tagihin.view.bill.paid

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.tagihin.data.remote.repository.PaidRepository

class PaidViewModel (val repo : PaidRepository) : ViewModel() {
    var shouldTriggerSomething: MutableLiveData<String> = MutableLiveData()
    var query : MutableLiveData<String> = MutableLiveData()

    private val repoResult = Transformations.map(shouldTriggerSomething) {
        repo.getPaidBill()
    }
    private val localSearch = Transformations.map(query) {
        repo.searchPaidBill(it)
    }
    val searchResult = Transformations.switchMap(localSearch){
        it.pagedList
    }
    val posts = Transformations.switchMap(repoResult) { it.pagedList }
    val networkState = Transformations.switchMap(repoResult) { it.networkState }
    val refreshState = Transformations.switchMap(repoResult) { it.refreshState }

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        val listing = repoResult.value
        listing?.retry?.invoke()
    }
}