package com.example.tagihin.data.datasource.pending.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.model.PendingBill
import com.example.tagihin.data.remote.model.UnpaidBill
import com.example.tagihin.utils.PreferencesHelper
import java.util.concurrent.Executor

class SearchPendingDataSourceFactory(
    private val api : ApiService,
    private val retryExecutor: Executor,
    private val query : String,
    private val type : String,
    private val pref : PreferencesHelper
) : DataSource.Factory<Int, PendingBill>() {
    val sourceLiveData = MutableLiveData<SearchPendingDataSource>()
    override fun create(): DataSource<Int, PendingBill> {
        val source =
            SearchPendingDataSource(
                api,
                query,
                type,
                retryExecutor,
                pref
            )
        sourceLiveData.postValue(source)
        return source
    }
}