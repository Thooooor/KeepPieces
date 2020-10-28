package com.keeppieces.android.logic.data

import androidx.annotation.ColorRes
import com.keeppieces.android.KeepPiecesApplication
import com.keeppieces.android.logic.Repository


class PrimaryCategoryRepository {
    private val context = KeepPiecesApplication.context
    private val primaryCategoryDao = AppDatabase.getDatabase(context).primaryCategoryDao()
    private val repository = com.keeppieces.android.logic.Repository

    fun createPrimaryCategory(primaryCategory: PrimaryCategory) {
        primaryCategoryDao.insertPrimaryCategory(primaryCategory)
    }

    fun getPrimaryCategory() = primaryCategoryDao.getPrimaryCategory()

    fun getDailyPrimaryList(bills: List<Bill>, color: String): MutableList<DailyPrimary> {
        val primaryList = mutableListOf<DailyPrimary>()
        for (bill in bills) {
            var isPrimaryExist = false
            for (primary in primaryList) {
                if (primary.primaryCategory == bill.primaryCategory) {
                    if (bill.type == "收入") {
                        primary.amount += bill.amount
                    } else {
                        primary.amount -= bill.amount
                    }
                    isPrimaryExist = true
                    break
                } else {
                    continue
                }
            }
            if (!isPrimaryExist) {
                val primaryIndex = primaryList.size
                val primaryColor = repository.getColorInt(color, primaryIndex)
                if (bill.type == "收入") {
                    primaryList.add(DailyPrimary(bill.primaryCategory, bill.amount, primaryColor))
                } else {
                    primaryList.add(DailyPrimary(bill.primaryCategory, -bill.amount, primaryColor))
                }
            } else {
                continue
            }
        }
        return primaryList
    }

    fun updatePrimaryCategory(primaryCategory: PrimaryCategory) {
        primaryCategoryDao.updatePrimaryCategory(primaryCategory)
    }

    fun deletePrimaryCategory(primaryCategory: PrimaryCategory) {
        primaryCategoryDao.deletePrimaryCategory(primaryCategory)
    }

    // 对 bills 按照一级类进行分类，返回结果的形式 MutableMap<String,List<Bill>>
    fun getPrimaryClassification(bills:List<Bill>):MutableMap<String,Pair<List<Bill>,Int>>{
        val primaryClassification = mutableMapOf<String,Pair<List<Bill>,Int>>()
        var count:Int = 0
        for(bill in bills){
            if(bill.primaryCategory !in primaryClassification) {
                primaryClassification[bill.primaryCategory] = Pair(mutableListOf(bill),count++)
            }
            else {
                primaryClassification[bill.primaryCategory] = Pair(
                    primaryClassification[bill.primaryCategory]!!.first.plus(bill),
                    primaryClassification[bill.primaryCategory]!!.second)
            }
        }
        return primaryClassification
    }

    fun getPrimarySummary(primaryClassification: MutableMap<String,Pair<List<Bill>,Int>>,
                          positiveColor: String,
                          negativeColor: String)
            : MutableList<GeneralPrimary> {
        val primaryList = mutableListOf<GeneralPrimary>()  // 存放最终结果
        for (primaryBill in primaryClassification) {
            val secondarySet:MutableSet<String> = mutableSetOf()
            var total = 0.00
            var income = 0.00
            var expenditure = 0.00
            val billList = primaryBill.value.first
            for (bill in billList) {
                secondarySet.add(bill.secondaryCategory)
                if (bill.type == "收入") {
                    income += bill.amount
                    total += bill.amount
                }
                if (bill.type == "支出") {
                    expenditure += bill.amount
                    total -= bill.amount
                }
            }
            val primaryColor = Repository.getColorInt(
                if (total>=0) {positiveColor} else {negativeColor}, primaryBill.value.second)
            // 这里的total是这个一级类下的所有账单的收支求和（收入为正，支出为负）
            primaryList.add(GeneralPrimary(primaryBill.key, total, primaryColor,
                                            income,expenditure,secondarySet.size,
                                            billList))
        }
        return primaryList
    }
}

data class DailyPrimary(
    val primaryCategory: String,
    var amount: Double,
    @ColorRes val color: Int,
)

data class GeneralPrimary(
    val primaryCategory: String,
    var amount: Double,
    @ColorRes val color: Int,
    var income: Double=0.00,
    var expenditure: Double=0.00,
    var secondaryNum:Int = 0,
    var secondaryList:List<Bill> = listOf()
)