package com.keeppieces.android.logic.data

import android.os.Build
import android.util.Log
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.keeppieces.android.KeepPiecesApplication
import java.time.LocalDate

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

    fun getPeriodBill(startDate: String, endDate: String) =
        billDao.getPeriodBill(startDate, endDate)

    fun getAccountPeriodBill(startDate: String, endDate: String, account: String) : LiveData<List<Bill>> {
        return billDao.getPeriodAccountBill(startDate, endDate, account)
    }

    fun getMemberPeriodBill(
        startDate: String,
        endDate: String,
        member: String
    ): LiveData<List<Bill>> {
        return billDao.getPeriodMemberBill(startDate, endDate, member)
    }

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

    fun getAllBill() = billDao.getAllBill()

    //    // startDateString、endDataString的格式：2020-10-14，返回区间内的账单信息
    fun getPeriodBillWithoutDao(
        startDateString: String,
        endDateString: String,
        allBills: List<Bill>
    ): MutableList<Bill> {
        val startDate = date2Int(startDateString)
        val endDate = date2Int(endDateString)
        val periodBills = mutableListOf<Bill>()
        for (bill in allBills) {
            Log.d("CheckBill", bill.date)
            val billDate = date2Int(bill.date)
            if (billDate in startDate..endDate) {
                periodBills.add(bill)
            }
        }
        return periodBills
    }

    private fun date2Int(date: String): Int = date.replace("-", "").toInt()

    fun getOneDaySummary(bills: List<Bill>, color: String): TodaySummary {
        val primaryList = mutableListOf<String>()
        val newBills = mutableListOf<GeneralBill>()
        var total = 0.00
        for (bill in bills) {
            val primary = bill.primaryCategory
            if (!primaryList.contains(primary)) {
                primaryList.add(primary)
            }
            val primaryIndex = primaryList.indexOf(primary)
            val primaryColorInt = repository.getColorInt(color, primaryIndex)
            newBills.add(GeneralBill(bill, primaryColorInt))
            when (bill.type) {
                "收入" -> total += bill.amount
                "支出" -> total -= bill.amount
                else -> total += 0
            }
        }
        return TodaySummary(total, newBills)
    }
//    说明：可以使用 getPeriodBill 获取数据库中 一个月 的数据；也可以使用 getPeriodBillWithoutDao，对传入的全部账单进行筛选从而获取一个月的数据
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getNowMonthBillList(date: LocalDate):LiveData<List<Bill>> {
//        // date 是当天的日期，2020-10-16 格式
//        val nowYear = date.year.toString()
//        val nowMonth = date.monthValue.toString()
//        val monthFirstDay = LocalDate.parse("$nowYear-$nowMonth-01")
//        val monthLastDay = monthFirstDay.plusMonths(1).minusDays(1)
//        return billDao.getPeriodBill(monthFirstDay.toString(), monthLastDay.toString())
//    }

    fun getABill(id: Long) = billDao.getABill(id)

    fun getBillById(billId:Long) = billDao.getBillById(billId)

    @RequiresApi(Build.VERSION_CODES.O)
    fun getBillLine(bills: List<Bill>, startDate: String, timeSpan:Int): MutableList<Double> {
        val dateList = mutableListOf(startDate)
        val pointList = mutableListOf(0.00)
        var localStartDate = LocalDate.parse(startDate)

        for (bill in bills) {
            val date = bill.date
            if (!dateList.contains(date)) {
                localStartDate = localStartDate.plusDays(1)
                while (date != localStartDate.toString()) {
                    dateList.add(localStartDate.toString())
                    pointList.add(0.00)
                    localStartDate = localStartDate.plusDays(1)
                }
                dateList.add(date)
                pointList.add(0.00)
            }
            val dateIndex = dateList.indexOf(date)
            if (bill.type == "支出")  pointList[dateIndex] += bill.amount
        }
        return pointList
    }

    fun getBillByGeneralBill(generalBill: GeneralBill): Bill {
        val bill = Bill(
            generalBill.date,generalBill.amount, generalBill.account,generalBill.member,
            generalBill.primaryCategory,generalBill.secondaryCategory,generalBill.type)
        bill.billId = generalBill.billId
        return bill
    }

//    fun getAllPrimaryCategoryBill(primaryCategory: String)
//            = billDao.getPrimaryCategoryBill(primaryCategory)

    fun getCategoryBillListInTimeSpan(startDate: String, endDate: String,primaryCategory:String,secondaryCategory:String? = null) =
        if (secondaryCategory == null) {
            billDao.getPrimaryCategoryBillInTimeSpan(startDate,endDate,primaryCategory)
        }
        else {
            billDao.getSecondaryCategoryBillInTimeSpan(startDate,endDate,primaryCategory,secondaryCategory)
        }
}

data class DailyOverview(val total: Double, val bills: List<GeneralBill>)
data class TodaySummary(val today_total: Double, val bills: List<GeneralBill>)

class GeneralBill(bill: Bill, colorInt: Int) {  // 比 Bill类多一个color
    val billId: Long = bill.billId
    val date: String = bill.date
    val amount: Double = bill.amount
    val account: String = bill.account
    val member: String = bill.member
    val primaryCategory: String = bill.primaryCategory
    val secondaryCategory: String = bill.secondaryCategory
    val type: String = bill.type

    @ColorRes
    val color: Int = colorInt
}

fun getGeneralBillSecondaryCategory(generalBill: GeneralBill): String =
    generalBill.secondaryCategory

fun getGeneralBillAmount(generalBill: GeneralBill): Double = generalBill.amount
fun getGeneralBillColorInt(generalBill: GeneralBill): Int = generalBill.color



