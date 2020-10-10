package com.keeppieces.android.logic.data

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.keeppieces.android.KeepPiecesApplication


class PrimaryCategoryRepository {
    private val context = KeepPiecesApplication.context
    private val primaryCategoryDao = AppDatabase.getDatabase(context).primaryCategoryDao()

    fun createPrimaryCategory(primaryCategory: PrimaryCategory) {
        primaryCategoryDao.insertPrimaryCategory(primaryCategory)
    }

    fun getPrimaryCategory() = primaryCategoryDao.getPrimaryCategory()

    fun updatePrimaryCategory(primaryCategory: PrimaryCategory) {
        primaryCategoryDao.updatePrimaryCategory(primaryCategory)
    }

    fun deletePrimaryCategory(primaryCategory: PrimaryCategory) {
        primaryCategoryDao.deletePrimaryCategory(primaryCategory)
    }
}