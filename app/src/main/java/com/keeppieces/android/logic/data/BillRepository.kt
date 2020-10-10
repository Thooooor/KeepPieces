package com.keeppieces.android.logic.data

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.keeppieces.android.KeepPiecesApplication


class BillRepository {
    private val context = KeepPiecesApplication.context
    private val billDao = AppDatabase.getDatabase(context).billDao()

    fun createBill(bill:Bill) {
        bill.billId = billDao.insertBill(bill)
    }

    fun getBill() = billDao.getBill()

    fun updateBill(bill: Bill) {
        billDao.updateBill(bill)
    }

    fun deleteBill(bill: Bill) {
        billDao.deleteBill(bill)
    }

    fun loadAllBillByAmount() = billDao.loadAllBillByAmount()
}