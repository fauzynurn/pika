package com.example.tagihin.view.workorder.pending

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.tagihin.data.remote.repository.PendingRepository
import com.example.tagihin.data.remote.repository.PendingWorkOrderRepository

class PendingWorkOrderViewModel (val repo : PendingWorkOrderRepository) : ViewModel() {
    var shouldTriggerSomething: MutableLiveData<String> = MutableLiveData()

    private val repoResult = Transformations.map(shouldTriggerSomething) {
        repo.getPendingWorkOrder(30)
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