package com.example.tagihin.data.remote.repository

import android.annotation.SuppressLint
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.example.tagihin.data.Listing
import com.example.tagihin.data.local.TagihinDb
import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.NetworkState
import com.example.tagihin.data.remote.boundarycallback.PendingBoundaryCallback
import com.example.tagihin.data.remote.boundarycallback.PendingWorkOrderBoundaryCallback
import com.example.tagihin.data.remote.model.*
import com.example.tagihin.utils.PreferencesHelper
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

//try with inMemoryDB first
class PendingWorkOrderRepository (val apiService : ApiService,
                                  val pref : PreferencesHelper,
                                  val db : TagihinDb,
                                  private val ioExecutor: Executor,
                                  private val networkPageSize: Int = DEFAULT_NETWORK_PAGE_SIZE){

    companion object {
        private const val DEFAULT_NETWORK_PAGE_SIZE = 10
    }

    /**
     * Inserts the response into the database while also assigning position indices to items.
     */
    private fun insertPendingResultIntoDb(body: MutableList<PendingWorkOrder>?) {
        body.let { bills ->
            db.runInTransaction {
                if (bills != null) {
                    db.posts().insertPendingWorkOrder(bills)
                }
            }
        }
    }

    /**
     * When refresh is called, we simply run a fresh network request and when it arrives, clear
     * the database table and insert all new items in a transaction.
     * <p>
     * Since the PagedList already uses a database bound data source, it will automatically be
     * updated after the database transaction is finished.
     */
    @SuppressLint("CheckResult")
    @MainThread
    private fun refresh(): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING
        apiService.getPendingWorkOrderBill(pref.getUsername()!!,pref.getPrivilege(),0,10)
            .subscribeOn(Schedulers.io())
            .subscribe({
                ioExecutor.execute {
                    db.runInTransaction {
                        db.posts().deleteAllPendingWorkOrder()
                        insertPendingResultIntoDb(it?.body()?.data?.toMutableList())
                    }
                    // since we are in bg thread now, post the result.
                    networkState.postValue(NetworkState.LOADED)
                }
            }, {
                networkState.value = NetworkState.error(it.message)
            })
        return networkState
    }

    fun getPendingWorkOrder(pageSize: Int): Listing<PendingWorkOrder> {
        val boundaryCallback =
            PendingWorkOrderBoundaryCallback(
                webservice = apiService,
                handleResponse = this::insertPendingResultIntoDb,
                ioExecutor = ioExecutor,
                networkPageSize = networkPageSize,
                page = 0,
                pref = pref
            )

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh()
        }
        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val livePagedList = db.posts().getPendingWorkOrder().toLiveData(
            pageSize = pageSize,
            boundaryCallback = boundaryCallback)

        return Listing(
            pagedList = livePagedList,
            networkState = boundaryCallback.networkState,
            retry = {
                boundaryCallback.helper.retryAllFailed()
            },
            refresh = {
                refreshTrigger.value = null
            },
            refreshState = refreshState
        )
    }
}