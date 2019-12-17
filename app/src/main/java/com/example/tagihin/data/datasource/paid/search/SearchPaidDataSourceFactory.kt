package com.example.tagihin.data.datasource.paid.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.tagihin.data.datasource.unpaid.search.SearchUnpaidDataSource
import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.model.PaidBill
import com.example.tagihin.data.remote.model.UnpaidBill
import com.example.tagihin.utils.PreferencesHelper
import java.util.concurrent.Executor

class SearchPaidDataSourceFactory(
    private val api : ApiService,
    private val retryExecutor: Executor,
    private val query : String,
    private val type : String,
    private val pref : PreferencesHelper
) : DataSource.Factory<Int, PaidBill>() {
    val sourceLiveData = MutableLiveData<SearchPaidDataSource>()
    override fun create(): DataSource<Int, PaidBill> {
        val source =
            SearchPaidDataSource(
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