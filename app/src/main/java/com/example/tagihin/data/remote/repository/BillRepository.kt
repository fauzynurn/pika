package com.example.tagihin.data.remote.repository

import android.annotation.SuppressLint
import android.content.Context
import com.example.tagihin.data.local.TagihinDb
import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.model.GeneralResponse
import com.example.tagihin.data.remote.model.TempBill
import com.example.tagihin.utils.ConnectivityManager
import com.example.tagihin.utils.DateUtils
import com.example.tagihin.utils.PreferencesHelper
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import timber.log.Timber
import java.util.concurrent.Executor

class BillRepository(
    val executor: Executor,
    val context: Context,
    val db: TagihinDb,
    val apiService: ApiService,
    val pref: PreferencesHelper
) {

    @SuppressLint("CheckResult")
    fun updateBill(
        statusBefore : String,
        billId: Int,
        status: String,
        date: String,
        note: String,
        type: String,
        name: String,
        orderCode: String,
        remoteSuccessCallback: (Boolean) -> Unit,
        remoteFailCallback: (String) -> Unit,
        localSuccessCallback: (Boolean) -> Unit
    ) {
        if (ConnectivityManager.isOnline(context)) {
            remoteUpdateBill(billId, status, date, note)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    remoteSuccessCallback(it.body()?.status!!)
                }, {
                    remoteFailCallback(it.message!!)
                })
        } else {
            TempBill(
                id = billId,
                status1 = status,
                statusBefore = statusBefore,
                date = date,
                note = note,
                dateAdded = DateUtils.getCurrentDate("dd-MM-yyyy HH:mm"),
                type = type,
                name = name,
                orderCode = orderCode
            ).let {
                executor.execute {
                    db.runInTransaction {
                        val id = db.posts().insertTempBill(it)
                        Timber.e("XXX")
                    }
                    localSuccessCallback(true)
                }
            }
        }
    }

    fun remoteUpdateBill(
        billId: Int,
        status: String,
        date: String,
        note: String
    ): Maybe<Response<GeneralResponse>> {
        return apiService.updateBill(
            pref.getUsername()!!, billId, status, date, note
        )
    }
}