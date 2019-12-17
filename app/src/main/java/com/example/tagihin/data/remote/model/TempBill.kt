package com.example.tagihin.data.remote.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tempbill",
    indices = [Index(value = ["index"], unique = true)])
data class TempBill(
    @PrimaryKey(autoGenerate = true)
    var index : Int = 0,
    var id : Int,
    var orderCode : String,
    var name : String,
    var status1 : String,
    var statusBefore : String,
    var date : String,
    var note : String,
    var type : String,
    var dateAdded : String
)

data class SavedBillRequestItem(
    var id : Int,
    var status1 : String,
    var tanggal : String,
    var note : String
)

data class SavedBillRequestBody(
    var username : String,
    var list : ArrayList<SavedBillRequestItem>
)