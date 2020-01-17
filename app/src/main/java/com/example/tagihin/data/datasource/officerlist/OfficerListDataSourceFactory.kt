package com.example.tagihin.data.datasource.officerlist

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.tagihin.data.datasource.unpaid.search.SearchUnpaidDataSource
import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.model.Officer
import com.example.tagihin.data.remote.model.PaidBill
import com.example.tagihin.data.remote.model.UnpaidBill
import com.example.tagihin.utils.PreferencesHelper
import java.util.concurrent.Executor

class OfficerListDataSourceFactory(
    private val api : ApiService,
    private val retryExecutor: Executor,
    private val query : String,
    private val pref : PreferencesHelper
) : DataSource.Factory<Int, Officer>() {
    val sourceLiveData = MutableLiveData<OfficerListDataSource>()
    override fun create(): DataSource<Int, Officer> {
        val source =
            OfficerListDataSource(
                api,
                retryExecutor,
                query,
                pref
            )
        sourceLiveData.postValue(source)
        return source
    }
}