package com.example.tagihin.data.remote.boundarycallback

import androidx.annotation.MainThread
import androidx.paging.PagedList
import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.model.*
import com.example.tagihin.utils.PagingRequestHelper
import com.example.tagihin.utils.PreferencesHelper
import com.example.tagihin.utils.createStatusLiveData
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class UnpaidWorkOrderBoundaryCallback(
    private val webservice: ApiService,
    private val handleResponse: (data : MutableList<UnpaidWorkOrder> ) -> Unit,
    private val ioExecutor: Executor,
    private val pref : PreferencesHelper,
    private var page : Int,
    private val networkPageSize: Int)
    : PagedList.BoundaryCallback<UnpaidWorkOrder>() {

    val helper = PagingRequestHelper(ioExecutor)
    val networkState = helper.createStatusLiveData()

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    @MainThread
    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            helper ->
            webservice.getUnpaidWorkOrderBill(pref.getUsername()!!,pref.getPrivilege(),0,10)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    insertItemsIntoDb(it.body()?.data?.toMutableList(),helper)
                    page += 10
                }, {
                    helper.recordFailure(it)
                })
        }
    }


    /**
     * User reached to the end of the list.
     */
    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: UnpaidWorkOrder) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            helper -> webservice.getUnpaidWorkOrderBill(pref.getUsername()!!,pref.getPrivilege(),page,10)
            .subscribeOn(Schedulers.io())
            .subscribe({
                insertItemsIntoDb(it.body()?.data?.toMutableList(),helper)
                page += 10
            }, {
                helper.recordFailure(it)
            })
        }
    }

    /**
     * every time it gets new items, boundary callback simply inserts them into the database and
     * paging library takes care of refreshing the list if necessary.
     */
    private fun insertItemsIntoDb(
        data: MutableList<UnpaidWorkOrder>?,
        it: PagingRequestHelper.Request.Callback) {
        ioExecutor.execute {
            if (data != null) {
                handleResponse(data)
                it.recordSuccess()
            }
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: UnpaidWorkOrder) {
        // ignored, since we only ever append to what's in the DB
    }

//    private fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback)
//            : Callback<BillResponse<List<PaidBill>>> {
//        return object : Callback<BillResponse<List<PaidBill>>> {
//            override fun onFailure(
//                call: Call<BillResponse<List<PaidBill>>>,
//                t: Throwable) {
//                it.recordFailure(t)
//            }
//
//            override fun onResponse(
//                call: Call<BillResponse<List<PaidBill>>>,
//                response: Response<BillResponse<List<PaidBill>>>
//            ) {
//                insertItemsIntoDb(response, it)
//            }
//        }
//    }
}