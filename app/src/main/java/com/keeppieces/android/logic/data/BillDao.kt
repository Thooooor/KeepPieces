package com.keeppieces.android.logic.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BillDao {
    @Insert
    fun insertBill(bill: Bill) : Long

    @Update
    fun updateBill(newBill: Bill)

    @Delete
    fun deleteBill(bill: Bill)

    @Query("SELECT * FROM bill WHERE date = :date ORDER BY bill_id")
    fun getOneDayBill(date: String): LiveData<List<Bill>>

    @Query("SELECT * FROM bill ORDER BY amount")
    fun loadAllBillByAmount(): LiveData<List<Bill>>
}