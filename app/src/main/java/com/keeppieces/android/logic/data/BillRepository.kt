package com.keeppieces.android.logic.data

import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.keeppieces.android.KeepPiecesApplication
import java.time.LocalDate
import java.time.Month

class BillRepository {
    private val context = KeepPiecesApplication.context
    private val billDao = AppDatabase.getDatabase(context).billDao()
    private val repository = com.keeppieces.android.logic.Repository

    fun insertBill(bill:Bill) {
        bill.billId = billDao.insertBill(bill)
    }

    fun getOneDayBill(date: String) = billDao.getOneDayBill(date)

    fun getOneDayOverview(bills: List<Bill>, color: String): DailyOverview {
        val dailyBills = billToNew(bills, color)
        var total = 0.0
        for (bill in dailyBills) {
            total += when (bill.type) {
                "收入" -> bill.amount
                "支出" -> -bill.amount
                else -> bill.amount
            }
        }
        return DailyOverview(total, dailyBills)
    }

    fun getPeriodBill(startDate: String, endDate: String) = billDao.getPeriodBill(startDate, endDate)

    private fun billToNew(bills: List<Bill>, color: String): MutableList<GeneralBill> {
        val primaryList = mutableListOf<String>()
        val newBillList = mutableListOf<GeneralBill>()
        for (bill in bills) {
            val primary = bill.primaryCategory
            if (!primaryList.contains(primary)) {
                primaryList.add(primary)
            }
            val primaryIndex = primaryList.indexOf(primary)
            val primaryColorInt = repository.getColorInt(color, primaryIndex)
            newBillList.add(GeneralBill(bill, primaryColorInt))
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

    fun getAllBills() = billDao.getAllBill()

//    // startDateString、endDataString的格式：2020-10-14，返回区间内的账单信息
//    fun getPeriodBill(startDateString: String, endDateString:String, allBills:List<Bill>):MutableList<Bill>{
//        val startDate = date2Int(startDateString)
//        val endDate = date2Int(endDateString)
//        val periodBills = mutableListOf<Bill>()
//        for(bill in allBills){
//            val billDate = date2Int(bill.date)
//            if (billDate in startDate..endDate) {
//                periodBills.add(bill)
//            }
//        }
//        return periodBills
//    }

//    private fun date2Int(date:String):Int = date.replace("-","").toInt()
fun getOneDaySummary(bills: List<Bill>, color: String): TodaySummary {
    val primaryList = mutableListOf<String>()
    val newBills = mutableListOf<GeneralBill>()
    var total= 0.00
    for (bill in bills) {
        val primary = bill.primaryCategory
        if (!primaryList.contains(primary)) {
            primaryList.add(primary)
        }
        val primaryIndex = primaryList.indexOf(primary)
        val primaryColorInt = repository.getColorInt(color, primaryIndex)
        newBills.add(GeneralBill(bill, primaryColorInt))
        when(bill.type) {
            "收入" -> total += bill.amount
            else -> total -= bill.amount
        }
    }
    return TodaySummary(total, newBills)
}

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNowMonthBillList(date: LocalDate):LiveData<List<Bill>> {
        // date 是当天的日期，2020-10-16 格式
        val nowYear = date.year.toString()
        val nowMonth = date.monthValue.toString()
        val monthFirstDay = LocalDate.parse("$nowYear-$nowMonth-01")
        val monthLastDay = monthFirstDay.plusMonths(1).minusDays(1)
        return billDao.getPeriodBill(monthFirstDay.toString(), monthLastDay.toString())
    }
}

data class DailyOverview(val total: Double, val bills: List<GeneralBill>)
data class TodaySummary(val today_total:Double, val bills:List<GeneralBill>)

class GeneralBill(bill: Bill, colorInt: Int) {
    val date: String = bill.date
    val amount: Double = bill.amount
    val account: String = bill.account
    val member: String = bill.member
    val primaryCategory: String = bill.primaryCategory
    val secondaryCategory: String = bill.secondaryCategory
    val type: String = bill.type
    @ColorRes val color: Int = colorInt
}
