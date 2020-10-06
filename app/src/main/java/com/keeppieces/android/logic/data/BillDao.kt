package com.keeppieces.android.logic.data

import androidx.room.*

@Dao
interface BillDao {
    @Insert
    fun insertBill(bill: Bill) : Long

    @Update
    fun updateBill(newBill: Bill)

    @Delete
    fun deleteBill(bill: Bill)

    @Query("select * from Bill")
    fun loadAllBills(): List<Bill>
}