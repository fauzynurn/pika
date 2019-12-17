package com.example.tagihin.data.datasource.paid.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.NetworkState
import com.example.tagihin.data.remote.model.PaidBill
import com.example.tagihin.data.remote.model.UnpaidBill
import com.example.tagihin.utils.PreferencesHelper
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

class SearchPaidDataSource(
    private val api: ApiService,
    private val query : String,
    private val type : String,
    private val retryExecutor: Executor,
    private val pref : PreferencesHelper
) : PageKeyedDataSource<Int, PaidBill>(){

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    private var disposable : Disposable? = null
    private var PAGE = 0
    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PaidBill>
    ) {
        disposable = api.searchPaid(pref.getUsername()!!,query,type,0,params.requestedLoadSize)
            .subscribeOn(Schedulers.io())
            .subscribe({
                retry = null
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
                PAGE += params.requestedLoadSize
                it?.body()?.data?.let { it1 -> callback.onResult(it1 , PAGE, params.requestedLoadSize) }
            }, {
                retry = {
                    loadInitial(params, callback)
                }
                val error = NetworkState.error(it.message ?: "unknown error")
                networkState.postValue(error)
                initialLoad.postValue(error)
            })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PaidBill>) {
        networkState.postValue(NetworkState.LOADING)
        disposable = api.searchPaid(pref.getUsername()!!,query,type,PAGE,params.requestedLoadSize)
            .subscribeOn(Schedulers.io())
            .subscribe({
                retry = null
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
                PAGE += params.requestedLoadSize
                it?.body()?.data?.let { it1 -> callback.onResult(it1 as MutableList<PaidBill>, PAGE) }
            }, {
                retry = {
                    loadAfter(params, callback)
                }
                val error = NetworkState.error(it.message ?: "unknown error")
                networkState.postValue(error)
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PaidBill>) {

    }

}