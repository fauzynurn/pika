package com.example.tagihin.data.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tagihin.data.remote.model.*
import io.reactivex.Maybe

@Dao
interface TagihinDao {
    //Insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPaidBill(posts : List<PaidBill>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPendingBill(posts : List<PendingBill>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUnpaidBill(posts : List<UnpaidBill>) : List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUnpaidWorkOrder(posts : List<UnpaidWorkOrder>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPendingWorkOrder(posts : List<PendingWorkOrder>)

    //Read
    @Query("SELECT * FROM paidbill")
    fun getPaidBill() : DataSource.Factory<Int,PaidBill>

    @Query("SELECT * FROM pendingbill")
    fun getPendingBill() : DataSource.Factory<Int,PendingBill>

    @Query("SELECT * FROM unpaidbill")
    fun getUnpaidBill() : DataSource.Factory<Int,UnpaidBill>

    @Query("SELECT * FROM unpaidbill")
    fun getUnpaidBillX() : List<UnpaidBill>

    @Query("SELECT * FROM pendingworkorder")
    fun getPendingWorkOrder() : DataSource.Factory<Int, PendingWorkOrder>

    @Query("SELECT * FROM unpaidworkorder")
    fun getUnpaidWorkOrder() : DataSource.Factory<Int,UnpaidWorkOrder>

    //search
    @Query("SELECT * FROM paidbill WHERE nama LIKE :query")
    fun searchPaidBill(query: String?): DataSource.Factory<Int,PaidBill>

    @Query("SELECT * FROM pendingbill WHERE nama LIKE :query")
    fun searchPendingBill(query: String?): DataSource.Factory<Int,PendingBill>

    @Query("SELECT * FROM unpaidbill WHERE nama LIKE :query")
    fun searchUnpaidBill(query: String?): DataSource.Factory<Int,UnpaidBill>

    @Query("SELECT * FROM unpaidworkorder WHERE nama LIKE :query")
    fun searchUnpaidWorkOrder(query: String?): DataSource.Factory<Int,UnpaidWorkOrder>

    @Query("SELECT * FROM pendingworkorder WHERE nama LIKE :query")
    fun searchPendingWorkOrder(query: String?): DataSource.Factory<Int,PendingWorkOrder>

    //delete
    @Query("DELETE FROM paidbill")
    fun deleteAllPaidBill()

    @Query("DELETE FROM pendingbill")
    fun deleteAllPendingBill()

    @Query("DELETE FROM unpaidbill")
    fun deleteAllUnpaidBill()

    @Query("DELETE FROM pendingworkorder")
    fun deleteAllPendingWorkOrder()

    @Query("DELETE FROM unpaidworkorder")
    fun deleteAllUnpaidWorkOrder()

    //insert temp request
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTempBill(bills : TempBill) : Long

    //get temp request
    @Query("SELECT * FROM tempbill WHERE statusBefore = :type LIMIT :size OFFSET :page")
    fun getTempRequest(type : String, page : Int, size : Int) : List<TempBill>

    @Query("SELECT * FROM tempbill WHERE statusBefore = :type")
    fun getAllRequest(type : String) : Maybe<MutableList<TempBill>>

    //delete all temp request
    @Query("DELETE FROM tempbill")
    fun deleteAllTempBill()
}