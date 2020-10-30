package com.keeppieces.android.ui.primaryCategory.overview

import androidx.lifecycle.ViewModel
import com.keeppieces.android.logic.data.*

class PrimaryCategoryOverViewViewModel:ViewModel() {
    private val billRepository = BillRepository()
    private val primaryCategoryRepository = PrimaryCategoryRepository()
    lateinit var primaryClassification:MutableMap<String,Pair<List<Bill>,Int>>  // 一级类 -> (一级类下所有的二级类账单（名字重复）,颜色)
    lateinit var primarySummary:MutableList<GeneralPrimary>  // 一级类 -> 它的名字，它的收支总和，它的颜色；用于展示大圆环
    lateinit var primaryTotalIncomeExpenditure:Triple<Double,Double,Double>
    fun billList(startDate: String, endDate: String) = billRepository.getPeriodBill(startDate, endDate)

    fun getPrimaryClassification(bills:List<Bill>) {
        primaryClassification = primaryCategoryRepository.getPrimaryClassification(bills)
    }

    // 得到每一个一级类的概要：收支总和、颜色（大圆环），并把这个概要信息存放到ViewModel中，并返回一个一级类总和
    fun getPrimarySummary(positiveColor:String, negativeColor:String) {
        primarySummary = primaryCategoryRepository.getPrimarySummary(primaryClassification,positiveColor,negativeColor)
        var total = 0.00
        var income = 0.00
        var expenditure = 0.00
        for (summary in primarySummary) {
            total += summary.amount // amount有正有负
            income += summary.income
            expenditure += summary.expenditure
        }
        primaryTotalIncomeExpenditure = Triple(total,income,expenditure)
    }
}