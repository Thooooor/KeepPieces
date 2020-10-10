package com.keeppieces.android.logic.data

import com.keeppieces.android.KeepPiecesApplication


class BillRepository {
    private val context = KeepPiecesApplication.context
    private val billDao = AppDatabase.getDatabase(context).billDao()
    fun createBill(bill:Bill) {
        bill.id = billDao.insertBill(bill)
    }

    fun loadBills() = billDao.loadAllBills()
}