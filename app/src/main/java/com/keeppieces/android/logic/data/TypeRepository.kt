package com.keeppieces.android.logic.data

import androidx.annotation.ColorRes
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.keeppieces.android.KeepPiecesApplication
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

    fun getDailyTypeList(bills: List<Bill>, color: String): MutableList<DailyType> {
        val typeList = mutableListOf<DailyType>()
        for (bill in bills) {
            var isTypeExist = false
            for (type in typeList) {
                if (type.type == bill.type) {
                    type.amount += bill.amount
                    isTypeExist = true
                    break
                } else {
                    continue
                }
            }
            if (!isTypeExist) {
                val primaryIndex = typeList.size
                val primaryColor = repository.getColorInt(color, primaryIndex)
                typeList.add(DailyType(bill.type, bill.amount, primaryColor))
            } else {
                continue
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

