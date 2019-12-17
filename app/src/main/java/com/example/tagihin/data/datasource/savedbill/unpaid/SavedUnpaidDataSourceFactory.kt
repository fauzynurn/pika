package com.example.tagihin.data.datasource.savedbill.unpaid

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.tagihin.data.local.TagihinDb
import com.example.tagihin.data.remote.model.TempBill

class SavedUnpaidDataSourceFactory(
    private val db : TagihinDb
) : DataSource.Factory<Int, TempBill>() {
    val sourceLiveData = MutableLiveData<SavedUnpaidDataSource>()
    override fun create(): DataSource<Int, TempBill> {
        val source = SavedUnpaidDataSource(db)
        sourceLiveData.postValue(source)
        return source
    }
}