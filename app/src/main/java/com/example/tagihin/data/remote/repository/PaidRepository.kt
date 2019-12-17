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
import com.example.tagihin.data.datasource.paid.search.SearchPaidDataSourceFactory
import com.example.tagihin.data.local.TagihinDb
import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.NetworkState
import com.example.tagihin.data.remote.boundarycallback.PaidBoundaryCallback
import com.example.tagihin.data.remote.model.*
import com.example.tagihin.utils.Consts
import com.example.tagihin.utils.PreferencesHelper
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

//try with inMemoryDB first
class PaidRepository (val apiService : ApiService,
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
    private fun insertPaidResultIntoDb(body: MutableList<PaidBill>?) {
        body.let { bills ->
            db.runInTransaction {
                if (bills != null) {
                    db.posts().insertPaidBill(bills)
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
        apiService.getPaidBill(pref.getUsername()!!,3,0,10)
            .subscribeOn(Schedulers.io())
            .subscribe({
                ioExecutor.execute {
                    db.runInTransaction {
                        db.posts().deleteAllPaidBill()
                        insertPaidResultIntoDb(it?.body()?.data)
                    }
                    // since we are in bg thread now, post the result.
                    networkState.postValue(NetworkState.LOADED)
                }
            }, {
                networkState.value = NetworkState.error(it.message)
            })
        return networkState
    }

    fun searchPaidBill(query : String) : Listing<PaidBill>{
        val sourceFactory =
            SearchPaidDataSourceFactory(
                apiService,
                ioExecutor,
                query,
                Consts.PAID,
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

    fun getPaidBill(): Listing<PaidBill> {
        val boundaryCallback =
            PaidBoundaryCallback(
                webservice = apiService,
                handleResponse = this::insertPaidResultIntoDb,
                ioExecutor = ioExecutor,
                pref = pref
            )

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh()
        }

        val config = Config(
            10,
            0
        )

        val livePagedList = LivePagedListBuilder(db.posts().getPaidBill(), config).setBoundaryCallback(boundaryCallback
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