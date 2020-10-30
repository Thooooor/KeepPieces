package com.keeppieces.android.ui.primaryCategory.detail

import androidx.lifecycle.ViewModel
import com.keeppieces.android.logic.data.*

class CategoryDetailActivityViewModel : ViewModel() {
    private val billRepository = BillRepository()
    private val secondaryCategoryRepository = SecondaryCategoryRepository()
    var incomeBillList:MutableList<Bill> = mutableListOf()
    var expenditureBillList:MutableList<Bill> = mutableListOf()
    lateinit var classification:MutableMap<String,Pair<List<Bill>,Int>>
    lateinit var categorySummary:MutableList<GeneralCategory>// 二级类 -> 它的名字，它的收支总和，它的颜色；用于展示大圆环
    var incomeTotal = 0.00
    var expenditureTotal = 0.00

    fun getCategoryBillInTimeSpan(
        startDate: String,
        endDate: String,
        category: String,
        level :Int
    ) = billRepository.getCategoryBillListInTimeSpan(startDate, endDate, category,level)

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

    fun getClassification(bills:List<Bill>,level:Int) {
        classification = secondaryCategoryRepository.getClassification(bills,level)
    }

    // 得到每一个一级类的概要：收支总和、颜色（大圆环），并把这个概要信息存放到ViewModel中，并返回一个一级类总和
    fun getCategorySummary(positiveColor:String,negativeColor:String){
        categorySummary = secondaryCategoryRepository.getCategorySummary(classification,positiveColor,negativeColor)
    }
}