package com.example.tagihin.data.remote.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.tagihin.data.datasource.savedbill.pending.SavedPendingDataSourceFactory
import com.example.tagihin.data.datasource.savedbill.unpaid.SavedUnpaidDataSourceFactory
import com.example.tagihin.data.local.TagihinDb
import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.model.*
import com.example.tagihin.utils.Consts
import com.example.tagihin.utils.PreferencesHelper
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

//try with inMemoryDB first
class SavedBillRepository(
    val db: TagihinDb,
    val apiService: ApiService,
    private val ioExecutor: Executor,
    val pref: PreferencesHelper
) {

    fun getSavedPendingBill(pageSize: Int): LiveData<PagedList<TempBill>> {
        val sourceFactory = SavedPendingDataSourceFactory(db)
        return LivePagedListBuilder(sourceFactory, pageSize).build()
    }

    fun getSavedUnpaidBill(pageSize: Int): LiveData<PagedList<TempBill>> {
        val sourceFactory = SavedUnpaidDataSourceFactory(db)
        return LivePagedListBuilder(sourceFactory, pageSize).build()
    }

    @SuppressLint("CheckResult")
    fun getAllSavedRequest(
        callback: (
            ArrayList<SavedBillRequestItem>
        ) -> Unit
        , converter: (
            MutableList<TempBill>, MutableList<TempBill>
        ) -> (ArrayList<SavedBillRequestItem>)
    ) {
        Maybe.zip(
            db.posts().getAllRequest(Consts.PENDING), db.posts().getAllRequest(Consts.UNPAID),
            BiFunction<MutableList<TempBill>, MutableList<TempBill>, ArrayList<SavedBillRequestItem>> { t1, t2 ->
                return@BiFunction converter(t1, t2)
            }
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                callback(it)
            }
    }

    //fun getAllSavedPending(): List<TempBill> = db.posts().getAllRequest(Consts.PENDING)

    @SuppressLint("CheckResult")
    fun sendRequest(
        list: ArrayList<SavedBillRequestItem>,
        successCallback: (Boolean) -> Unit,
        failCallback: (String) -> Unit
    ) {
        apiService.sendSavedRequest(SavedBillRequestBody(pref.getUsername()!!, list))
            .subscribeOn(Schedulers.io())
            .subscribe({
                successCallback(it.body()?.status!!)
            }, {
                failCallback(it.message!!)
            })
    }

    fun deleteAllSavedRecord() {
        ioExecutor.execute {
            db.posts().deleteAllTempBill()
        }
    }
}