package com.keeppieces.android.logic.data

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.keeppieces.android.KeepPiecesApplication


class TypeRepository {
    private val context = KeepPiecesApplication.context
    private val typeDao = AppDatabase.getDatabase(context).typeDao()

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
}