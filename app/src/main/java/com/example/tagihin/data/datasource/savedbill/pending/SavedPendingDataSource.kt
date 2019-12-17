package com.example.tagihin.data.datasource.savedbill.pending

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.tagihin.data.local.TagihinDb
import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.NetworkState
import com.example.tagihin.data.remote.model.PendingBill
import com.example.tagihin.data.remote.model.TempBill
import com.example.tagihin.utils.Consts
import com.example.tagihin.utils.PreferencesHelper
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

class SavedPendingDataSource(
    private val db: TagihinDb
) : PageKeyedDataSource<Int, TempBill>() {
    var PAGE = 0
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, TempBill>
    ) {
        callback.onResult(db.posts().getTempRequest(Consts.PENDING,0, params.requestedLoadSize), 0, params.requestedLoadSize)
        PAGE += 10
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, TempBill>) {
        callback.onResult(db.posts().getTempRequest(Consts.PENDING, PAGE, params.requestedLoadSize), params.key)
        PAGE += 10
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, TempBill>) {

    }

}