package com.keeppieces.android.logic.data

import androidx.annotation.ColorRes
import com.keeppieces.android.KeepPiecesApplication

class BillRepository {
    private val context = KeepPiecesApplication.context
    private val billDao = AppDatabase.getDatabase(context).billDao()
    private val repository = com.keeppieces.android.logic.Repository

    fun createBill(bill:Bill) {
        bill.billId = billDao.insertBill(bill)
    }

    fun getOneDayBill(date: String) = billDao.getOneDayBill(date)

    fun getOneDayOverview(bills: List<Bill>, color: String): DailyOverview {
        val newBills = billToNew(bills, color)
        val total = newBills.sumByDouble { it.amount }
        return DailyOverview(total, newBills)
    }

    private fun billToNew(bills: List<Bill>, color: String): MutableList<DailyBill> {
        val primaryList = mutableListOf<String>()
        val newBillList = mutableListOf<DailyBill>()
        for (bill in bills) {
            val primary = bill.primaryCategory
            if (!primaryList.contains(primary)) {
                primaryList.add(primary)
            }
            val primaryIndex = primaryList.indexOf(primary)
            val primaryColorInt = repository.getColorInt(color, primaryIndex)
            newBillList.add(DailyBill(bill, primaryColorInt))
        }
        return newBillList
    }

    fun updateBill(bill: Bill) {
        billDao.updateBill(bill)
    }

    fun deleteBill(bill: Bill) {
        billDao.deleteBill(bill)
    }

    fun loadAllBillByAmount() = billDao.loadAllBillByAmount()
}

data class DailyOverview(val total: Double, val bills: List<DailyBill>)

class DailyBill(bill: Bill, colorInt: Int) {
    val date: String = bill.date
    val amount: Double = bill.amount
    val account: String = bill.account
    val member: String = bill.member
    val primaryCategory: String = bill.primaryCategory
    val secondaryCategory: String = bill.secondaryCategory
    val type: String = bill.type
    @ColorRes val color: Int = colorInt
}