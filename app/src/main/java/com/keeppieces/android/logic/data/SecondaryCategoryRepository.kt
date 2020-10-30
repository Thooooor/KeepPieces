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

    fun getClassification(bills:List<Bill>,level:Int):MutableMap<String,Pair<List<Bill>,Int>>{
        val classification = mutableMapOf<String,Pair<List<Bill>,Int>>()
        var category:String
        var count:Int = 0  // color
        for(bill in bills){
            category = if(level == 1) { bill.primaryCategory } else {bill.secondaryCategory}
            if (category !in classification){
                classification[category] = Pair(mutableListOf(bill),count++)
            }
            else {
                classification[category] = Pair(
                    classification[category]!!.first.plus(bill),
                    classification[category]!!.second)
            }
        }
        return classification
    }

    fun getCategorySummary(classification:MutableMap<String,Pair<List<Bill>,Int>>,positiveColor:String,negativeColor:String):MutableList<GeneralCategory>{
        val categorySummaryList = mutableListOf<GeneralCategory>()  // 存放最终结果
        for (categoryMap in classification) {
            val billList = categoryMap.value.first
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
                if (total>=0) {positiveColor} else {negativeColor}, categoryMap.value.second)
            categorySummaryList.add(GeneralCategory(categoryMap.key,total,color))
        }
        return categorySummaryList
    }
}

data class GeneralCategory(
    val category: String,
    var amount: Double,  // 带正负号
    @ColorRes val color: Int
)