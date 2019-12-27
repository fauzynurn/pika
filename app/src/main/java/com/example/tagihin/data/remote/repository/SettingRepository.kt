package com.example.tagihin.data.remote.repository

import android.annotation.SuppressLint
import com.example.tagihin.data.local.TagihinDb
import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.model.LoginResponse
import com.example.tagihin.utils.PreferencesHelper
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.concurrent.Executor

class SettingRepository(
    val executor: Executor,
    val db: TagihinDb,
    val pref: PreferencesHelper,
    val apiService: ApiService
) {
    @SuppressLint("CheckResult")
    fun resetWo(callback: (Boolean) -> Unit) {
        apiService.resetWo(
            pref.getUsername()!!, pref.getPrivilege()
        )
            .subscribeOn(Schedulers.io())
            .subscribe({
                callback(it?.body()?.status!!)
            }, {
                callback(false)
            })
    }

    @SuppressLint("CheckResult")
    fun resetBill(callback: (Boolean) -> Unit) {
        apiService.resetBill(
            pref.getUsername()!!, pref.getPrivilege()
        )
            .subscribeOn(Schedulers.io())
            .subscribe({
                callback(it?.body()?.status!!)
            }, {
                callback(false)
            })
    }

    fun resetLocalBill() {
        executor.execute {
            db.runInTransaction {
                db.posts().deleteAllPaidBill()
                db.posts().deleteAllPendingBill()
                db.posts().deleteAllUnpaidBill()
            }
        }
    }

    fun resetLocalWo() {
        executor.execute {
            db.runInTransaction {
                db.posts().deleteAllPendingWorkOrder()
                db.posts().deleteAllUnpaidBill()
            }
        }
    }
}
