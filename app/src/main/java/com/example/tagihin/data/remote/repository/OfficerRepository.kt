package com.example.tagihin.data.remote.repository

import android.annotation.SuppressLint
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import com.example.tagihin.data.Listing
import com.example.tagihin.data.datasource.officerlist.OfficerListDataSourceFactory
import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.NetworkState
import com.example.tagihin.data.remote.model.Officer
import com.example.tagihin.utils.Consts
import com.example.tagihin.utils.PreferencesHelper
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

//try with inMemoryDB first
class OfficerRepository (val apiService : ApiService,
                      val pref : PreferencesHelper,
                      private val ioExecutor: Executor,
                      private val networkPageSize: Int = DEFAULT_NETWORK_PAGE_SIZE){

    companion object {
        private const val DEFAULT_NETWORK_PAGE_SIZE = 10
    }

//    /**
//     * When refresh is called, we simply run a fresh network request and when it arrives, clear
//     * the database table and insert all new items in a transaction.
//     * <p>
//     * Since the PagedList already uses a database bound data source, it will automatically be
//     * updated after the database transaction is finished.
//     */
//    @SuppressLint("CheckResult")
//    @MainThread
//    private fun refresh(): LiveData<NetworkState> {
//        val networkState = MutableLiveData<NetworkState>()
//        networkState.value = NetworkState.LOADING
//        apiService.getPaidBill(pref.getUsername()!!,pref.getPrivilege(),0,10)
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                ioExecutor.execute {
//                    db.runInTransaction {
//                        db.posts().deleteAllPaidBill()
//                        insertPaidResultIntoDb(it?.body()?.data)
//                    }
//                    // since we are in bg thread now, post the result.
//                    networkState.postValue(NetworkState.LOADED)
//                }
//            }, {
//                networkState.value = NetworkState.error(it.message)
//            })
//        return networkState
//    }

    //Not implemented at this time
    fun getOfficerList(query : String) : Listing<Officer> {
        val sourceFactory =
            OfficerListDataSourceFactory(
                apiService,
                ioExecutor,
                query,
                pref
            )
        val config = Config(
            10,
            0
        )
        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val livePagedList = LivePagedListBuilder(sourceFactory,
            config
        ).build()

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }
        return Listing(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.networkState
            },
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            },
            refreshState = refreshState
        )
    }

//    fun getOfficerList() : Listing<Officer> {
//        val sourceFactory =
//            OfficerListDataSourceFactory(
//                apiService,
//                ioExecutor,
//                pref
//            )
//        val config = Config(
//            10,
//            0
//        )
//        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
//        val livePagedList = LivePagedListBuilder(sourceFactory,
//            config
//        ).build()
//
//        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
//            it.initialLoad
//        }
//        return Listing(
//            pagedList = livePagedList,
//            networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
//                it.networkState
//            },
//            retry = {
//                sourceFactory.sourceLiveData.value?.retryAllFailed()
//            },
//            refresh = {
//                sourceFactory.sourceLiveData.value?.invalidate()
//            },
//            refreshState = refreshState
//        )
//    }
//    fun getOfficerList(): Listing<Officer> {
//        val boundaryCallback =
//            OfficerListBoundaryCallback(
//                webservice = apiService,
//                handleResponse = this::insertPaidResultIntoDb,
//                ioExecutor = ioExecutor,
//                pref = pref
//            )
//
//        val refreshTrigger = MutableLiveData<Unit>()
//        val refreshState = Transformations.switchMap(refreshTrigger) {
//            refresh()
//        }
//
//        val config = Config(
//            10,
//            0
//        )
//
//        val livePagedList = LivePagedListBuilder(db.posts().getPaidBill(), config).setBoundaryCallback(boundaryCallback
//        ).build()
//
//        return Listing(
//            pagedList = livePagedList,
//            networkState = boundaryCallback.networkState,
//            retry = {
//                boundaryCallback.helper.retryAllFailed()
//            },
//            refresh = {
//                refreshTrigger.value = null
//            },
//            refreshState = refreshState
//        )
//    }
}