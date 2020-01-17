package com.example.tagihin.data.remote.repository

import android.annotation.SuppressLint
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import androidx.paging.toLiveData
import com.example.tagihin.data.Listing
import com.example.tagihin.data.datasource.pending.search.SearchPendingDataSourceFactory
import com.example.tagihin.data.local.TagihinDb
import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.NetworkState
import com.example.tagihin.data.remote.boundarycallback.PendingBoundaryCallback
import com.example.tagihin.data.remote.model.*
import com.example.tagihin.utils.Consts
import com.example.tagihin.utils.PreferencesHelper
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.concurrent.Executor

//try with inMemoryDB first
class PendingRepository (val apiService : ApiService,
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
    private fun insertPendingResultIntoDb(body: MutableList<PendingBill>?) {
        body.let { bills ->
            db.runInTransaction {
                if (bills != null) {
                    db.posts().insertPendingBill(bills)
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
        apiService.getPendingBill(pref.getUsername()!!,pref.getPrivilege(),0,10)
            .subscribeOn(Schedulers.io())
            .subscribe({
                ioExecutor.execute {
                    db.runInTransaction {
                        db.posts().deleteAllPendingBill()
                        insertPendingResultIntoDb(it?.body()?.data)
                    }
                    // since we are in bg thread now, post the result.
                    networkState.postValue(NetworkState.LOADED)
                }
            }, {
                networkState.value = NetworkState.error(it.message)
            })
        return networkState
    }

    fun moveToWO(list : HashMap<String,Int>) : Maybe<Response<GeneralResponse>> {
        return apiService.moveToWO(
            pref.getUsername()!!,pref.getPrivilege(),list
        )
    }

    fun searchPendingBill(query : String) : Listing<PendingBill>{
        val sourceFactory =
            SearchPendingDataSourceFactory(
                apiService,
                ioExecutor,
                query,
                Consts.PENDING_ENG,
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

    fun transferBill(username : String, list : HashMap<String,Int>) : Maybe<Response<GeneralResponse>> {
        return apiService.transferBill(
            pref.getUsername()!!,pref.getPrivilege(),username,list
        )
    }

    fun getPendingBill(): Listing<PendingBill> {
        val boundaryCallback =
            PendingBoundaryCallback(
                webservice = apiService,
                handleResponse = this::insertPendingResultIntoDb,
                ioExecutor = ioExecutor,
                pref = pref
            )

        val config = Config(
            10,
            0
        )

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh()
        }

        val livePagedList = LivePagedListBuilder(db.posts().getPendingBill(), config).setBoundaryCallback(boundaryCallback
        ).build()

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