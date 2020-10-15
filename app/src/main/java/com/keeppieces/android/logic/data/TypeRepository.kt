package com.keeppieces.android.logic.data

import androidx.annotation.ColorRes
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.keeppieces.android.KeepPiecesApplication
import com.keeppieces.android.R
import com.keeppieces.android.logic.Repository


class TypeRepository {
    private val context = KeepPiecesApplication.context
    private val typeDao = AppDatabase.getDatabase(context).typeDao()
    private val repository = Repository

    fun createType(type: Type) = typeDao.insertType(type)

    fun getType() {
        typeDao.getType()
    }

    fun updateType(type: Type) {
        typeDao.updateType(type)
    }

    fun deleteType(type: Type) {
        typeDao.deleteType(type)
    }

    fun getDailyTypeList(bills: List<Bill>): List<DailyType> {
        val income = "收入"
        val outcome = "支出"
        val typeList = listOf<DailyType>(
            DailyType(income, 0.0, R.color.yellow_600),
            DailyType(outcome, 0.0, R.color.dark_green)
        )
        for (bill in bills) {
            when (bill.type) {
                income -> typeList[0].amount += bill.amount
                outcome -> typeList[1].amount += bill.amount
            }
        }
        return typeList
    }
}

data class DailyType(
    val type: String,
    var amount: Double,
    @ColorRes val color: Int
)

