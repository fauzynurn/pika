package com.example.tagihin.view.workorder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.tagihin.data.remote.repository.PendingWorkOrderRepository
import com.example.tagihin.data.remote.repository.UnpaidWorkOrderRepository

class WorkOrderViewModel(val pendingRepo : PendingWorkOrderRepository, unpaidRepo : UnpaidWorkOrderRepository) : ViewModel(){
    var shouldTriggerSomethingPending: MutableLiveData<String> = MutableLiveData()

    private val pendingResult = Transformations.map(shouldTriggerSomethingPending) {
        pendingRepo.getPendingWorkOrder(30)
    }
    val pendingPosts = Transformations.switchMap(pendingResult) { it.pagedList }
    val pendingNetworkState = Transformations.switchMap(pendingResult) { it.networkState }
    val pendingRefreshState = Transformations.switchMap(pendingResult) { it.refreshState }


    var shouldTriggerSomethingUnpaid: MutableLiveData<String> = MutableLiveData()

    private val unpaidResult = Transformations.map(shouldTriggerSomethingPending) {
        unpaidRepo.getUnpaidWorkOrder(30)
    }
    val unpaidPosts = Transformations.switchMap(unpaidResult) { it.pagedList }
    val unpaidNetworkState = Transformations.switchMap(unpaidResult) { it.networkState }
    val unpaidRefreshState = Transformations.switchMap(unpaidResult) { it.refreshState }

    fun refreshPending() {
        pendingResult.value?.refresh?.invoke()
    }

    fun retryPending() {
        val listing = pendingResult.value
        listing?.retry?.invoke()
    }

    fun refreshUnpaid() {
        unpaidResult.value?.refresh?.invoke()
    }

    fun refreshAll(){
        refreshPending()
        refreshUnpaid()
    }

    fun retryUnpaid() {
        val listing = unpaidResult.value
        listing?.retry?.invoke()
    }
}
