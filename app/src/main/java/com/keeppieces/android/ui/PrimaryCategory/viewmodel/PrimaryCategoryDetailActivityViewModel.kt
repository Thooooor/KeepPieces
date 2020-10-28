package com.keeppieces.android.ui.PrimaryCategory.viewmodel

import androidx.lifecycle.ViewModel
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.logic.data.BillRepository
import com.keeppieces.android.logic.data.GeneralSecondary
import com.keeppieces.android.logic.data.SecondaryCategoryRepository

class PrimaryCategoryDetailActivityViewModel : ViewModel() {
    private val billRepository = BillRepository()
    private val secondaryCategoryRepository = SecondaryCategoryRepository()
    var incomeBillList:MutableList<Bill> = mutableListOf()
    var expenditureBillList:MutableList<Bill> = mutableListOf()
    lateinit var secondaryClassification:MutableMap<String,Pair<List<Bill>,Int>>
    lateinit var secondaryCategorySummary:MutableList<GeneralSecondary>// 二级类 -> 它的名字，它的收支总和，它的颜色；用于展示大圆环
    var incomeTotal = 0.00
    var expenditureTotal = 0.00
    fun getPrimaryCategoryBillInTimeSpan(
        startDate: String,
        endDate: String,
        primaryCategory: String
    ) = billRepository.getPrimaryCategoryBillInTimeSpan(startDate, endDate, primaryCategory)

    fun separateBillList(billList:List<Bill>){
        incomeBillList = mutableListOf()
        expenditureBillList = mutableListOf()
        incomeTotal = 0.00
        expenditureTotal = 0.00
        for (bill in billList){
            if (bill.type == "收入"){
                incomeBillList.add(bill)
                incomeTotal += bill.amount
            }
            if (bill.type == "支出"){
                expenditureBillList.add(bill)
                expenditureTotal += bill.amount
            }
        }
    }

    fun getSecondaryClassification(bills:List<Bill>) {
        secondaryClassification = secondaryCategoryRepository.getSecondaryClassification(bills)
    }

    // 得到每一个一级类的概要：收支总和、颜色（大圆环），并把这个概要信息存放到ViewModel中，并返回一个一级类总和
    fun getSecondaryCategorySummary(positiveColor:String,negativeColor:String){
        secondaryCategorySummary = secondaryCategoryRepository.getSecondarySummary(secondaryClassification,positiveColor,negativeColor)
    }
}