package com.example.tagihin.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tagihin.data.remote.model.*

@Database(
    entities = [PaidBill::class, PendingBill::class, UnpaidBill::class, UnpaidWorkOrder::class, PendingWorkOrder::class, TempBill::class],
    version = 1,
    exportSchema = false
)
abstract class TagihinDb : RoomDatabase() {
    companion object {
        fun create(context: Context, useInMemory : Boolean): TagihinDb {
            val databaseBuilder = if(useInMemory) {
                Room.inMemoryDatabaseBuilder(context, TagihinDb::class.java)
            } else {
                Room.databaseBuilder(context, TagihinDb::class.java, "tagihin.db")
            }
            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun posts(): TagihinDao
}