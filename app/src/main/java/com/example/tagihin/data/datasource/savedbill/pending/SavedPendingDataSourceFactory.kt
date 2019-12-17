package com.example.tagihin.data.datasource.savedbill.pending

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.tagihin.data.local.TagihinDb
import com.example.tagihin.data.remote.model.TempBill

class SavedPendingDataSourceFactory(
    private val db : TagihinDb
) : DataSource.Factory<Int, TempBill>() {
    val sourceLiveData = MutableLiveData<SavedPendingDataSource>()
    override fun create(): DataSource<Int, TempBill> {
        val source = SavedPendingDataSource(db)
        sourceLiveData.postValue(source)
        return source
    }
}