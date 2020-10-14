package com.keeppieces.android.logic.data

import androidx.annotation.ColorRes
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.keeppieces.android.KeepPiecesApplication


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
                    primary.amount += bill.amount
                    isPrimaryExist = true
                    break
                } else {
                    continue
                }
            }
            if (!isPrimaryExist) {
                val primaryIndex = primaryList.size
                val primaryColor = repository.getColorInt(color, primaryIndex)
                primaryList.add(DailyPrimary(bill.primaryCategory, bill.amount, primaryColor))
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
}

data class DailyPrimary(
    val primaryCategory: String,
    var amount: Double,
    @ColorRes val color: Int
)