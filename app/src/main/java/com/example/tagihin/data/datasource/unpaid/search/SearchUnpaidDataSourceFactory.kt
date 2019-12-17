package com.example.tagihin.data.datasource.unpaid.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.model.UnpaidBill
import com.example.tagihin.utils.PreferencesHelper
import java.util.concurrent.Executor

class SearchUnpaidDataSourceFactory(
    private val api : ApiService,
    private val retryExecutor: Executor,
    private val query : String,
    private val type : String,
    private val pref : PreferencesHelper
) : DataSource.Factory<Int, UnpaidBill>() {
    val sourceLiveData = MutableLiveData<SearchUnpaidDataSource>()
    override fun create(): DataSource<Int, UnpaidBill> {
        val source =
            SearchUnpaidDataSource(
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