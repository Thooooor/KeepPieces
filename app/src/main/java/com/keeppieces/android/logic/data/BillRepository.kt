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

    fun getOneDayOverview(bills: List<Bill>): DailyOverview {
        val newBills = billToNew(bills)
        val total = newBills.sumByDouble { it.amount }
        return DailyOverview(total, newBills)
    }

    private fun billToNew(bills: List<Bill>): MutableList<NewBill> {
        val primaryList = mutableListOf<String>()
        val newBillList = mutableListOf<NewBill>()
        for (bill in bills) {
            val primary = bill.primaryCategory
            if (!primaryList.contains(primary)) {
                primaryList.add(primary)
            }
            val primaryIndex = primaryList.indexOf(primary)
            val primaryColorInt = repository.getColorInt("green", primaryIndex)
            newBillList.add(NewBill(bill, primaryColorInt))
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

data class DailyOverview(val total: Double, val bills: List<NewBill>)

class NewBill(bill: Bill, colorInt: Int) {
    val date: String = bill.date
    val amount: Double = bill.amount
    val account: String = bill.account
    val member: String = bill.member
    val primaryCategory: String = bill.primaryCategory
    val secondaryCategory: String = bill.secondaryCategory
    val type: String = bill.type
    @ColorRes val color: Int = colorInt
}