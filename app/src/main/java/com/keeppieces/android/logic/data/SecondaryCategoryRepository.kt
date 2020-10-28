package com.keeppieces.android.logic.data

import androidx.annotation.ColorRes
import com.keeppieces.android.KeepPiecesApplication
import com.keeppieces.android.logic.Repository


class SecondaryCategoryRepository {
    private val context = KeepPiecesApplication.context
    private val secondaryCategoryDao = AppDatabase.getDatabase(context).secondaryCategoryDao()

    fun createSecondaryCategory(secondaryCategory: SecondaryCategory) {
        secondaryCategoryDao.insertSecondaryCategory(secondaryCategory)
    }

    fun getSecondaryCategory() = secondaryCategoryDao.getSecondaryCategory()

    fun updateSecondaryCategory(secondaryCategory: SecondaryCategory) {
        secondaryCategoryDao.updateSecondaryCategory(secondaryCategory)
    }

    fun deleteSecondaryCategory(secondaryCategory: SecondaryCategory) {
        secondaryCategoryDao.deleteSecondaryCategory(secondaryCategory)
    }

    fun getSecondaryClassification(bills:List<Bill>):MutableMap<String,Pair<List<Bill>,Int>>{
        val secondaryClassification = mutableMapOf<String,Pair<List<Bill>,Int>>()
        var count:Int = 0
        for(bill in bills){
            if(bill.secondaryCategory !in secondaryClassification) {
                secondaryClassification[bill.secondaryCategory] = Pair(mutableListOf(bill),count++)
            }
            else {
                secondaryClassification[bill.secondaryCategory] = Pair(
                    secondaryClassification[bill.secondaryCategory]!!.first.plus(bill),
                    secondaryClassification[bill.secondaryCategory]!!.second)
            }
        }
        return secondaryClassification
    }

    fun getSecondarySummary(secondaryClassification:MutableMap<String,Pair<List<Bill>,Int>>,positiveColor:String,negativeColor:String):MutableList<GeneralSecondary>{
        val secondaryList = mutableListOf<GeneralSecondary>()  // 存放最终结果
        for (secondaryBill in secondaryClassification) {
            val billList = secondaryBill.value.first
            var total = 0.00
            var income = 0.00
            var expenditure = 0.00
            for (bill in billList) {
                if (bill.type == "收入") {
                    income += bill.amount
                    total += bill.amount
                }
                if (bill.type == "支出") {
                    expenditure += bill.amount
                    total -= bill.amount
                }
            }
            val color = Repository.getColorInt(
                if (total>=0) {positiveColor} else {negativeColor}, secondaryBill.value.second)
            secondaryList.add(GeneralSecondary(secondaryBill.key,total,color))
        }
        return secondaryList
    }
}

data class GeneralSecondary(
    val secondaryCategory: String,
    var amount: Double,  // 带正负号
    @ColorRes val color: Int
)