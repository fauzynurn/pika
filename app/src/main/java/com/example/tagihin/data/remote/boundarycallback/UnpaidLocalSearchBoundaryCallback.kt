//package com.example.tagihin.data.remote.boundarycallback
//
//import androidx.annotation.MainThread
//import androidx.paging.PagedList
//import com.example.tagihin.data.local.TagihinDb
//import com.example.tagihin.data.remote.ApiService
//import com.example.tagihin.data.remote.model.BaseBill
//import com.example.tagihin.data.remote.model.BillResponse
//import com.example.tagihin.data.remote.model.PaidBill
//import com.example.tagihin.data.remote.model.UnpaidBill
//import com.example.tagihin.utils.PagingRequestHelper
//import com.example.tagihin.utils.PreferencesHelper
//import com.example.tagihin.utils.createStatusLiveData
//import io.reactivex.schedulers.Schedulers
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.util.concurrent.Executor
//
//class UnpaidLocalSearchBoundaryCallback(
//    private val db: TagihinDb,
//    private val query : String,
//    private val ioExecutor: Executor)
//    : PagedList.BoundaryCallback<UnpaidBill>() {
//
//    val helper = PagingRequestHelper(ioExecutor)
//    val networkState = helper.createStatusLiveData()
//
//    /**
//     * Database returned 0 items. We should query the backend for more items.
//     */
//    @MainThread
//    override fun onZeroItemsLoaded() {
//        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
//            db.posts().searchUnpaidBill(query)
//        }
//    }
//
//
//    /**
//     * User reached to the end of the list.
//     */
//    @MainThread
//    override fun onItemAtEndLoaded(itemAtEnd: UnpaidBill) {
//        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
//            helper -> webservice.getUnpaidBill(pref.getUsername()!!,3,itemAtEnd.index ?: 0,10)
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                insertItemsIntoDb(it.body()?.data,helper)
//            }, {
//                helper.recordFailure(it)
//            })
//        }
//    }
//
//    override fun onItemAtFrontLoaded(itemAtFront: UnpaidBill) {
//        // ignored, since we only ever append to what's in the DB
//    }
//
////    private fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback)
////            : Callback<BillResponse<List<PaidBill>>> {
////        return object : Callback<BillResponse<List<PaidBill>>> {
////            override fun onFailure(
////                call: Call<BillResponse<List<PaidBill>>>,
////                t: Throwable) {
////                it.recordFailure(t)
////            }
////
////            override fun onResponse(
////                call: Call<BillResponse<List<PaidBill>>>,
////                response: Response<BillResponse<List<PaidBill>>>
////            ) {
////                insertItemsIntoDb(response, it)
////            }
////        }
////    }
//}