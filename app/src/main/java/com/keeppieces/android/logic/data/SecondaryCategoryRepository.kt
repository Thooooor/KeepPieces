package com.keeppieces.android.logic.data

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.keeppieces.android.KeepPiecesApplication


class SecondaryCategoryRepository {
    private val context = KeepPiecesApplication.context
    private val secondaryCategoryDao = AppDatabase.getDatabase(context).secondaryCategoryDao()

    fun createSecondaryCategory(secondaryCategory: SecondaryCategory){
        secondaryCategoryDao.insertSecondaryCategory(secondaryCategory)
    }

    fun getSecondaryCategory() = secondaryCategoryDao.getSecondaryCategory()

    fun updateSecondaryCategory(secondaryCategory: SecondaryCategory) {
        secondaryCategoryDao.updateSecondaryCategory(secondaryCategory)
    }

    fun deleteSecondaryCategory(secondaryCategory: SecondaryCategory) {
        secondaryCategoryDao.deleteSecondaryCategory(secondaryCategory)
    }
}