package com.keeppieces.android.logic.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface PrimaryCategoryDao {
    @Insert
    fun insertPrimaryCategory(primaryCategory: PrimaryCategory): Long

    @Update
    fun updatePrimaryCategory(newPrimaryCategory: PrimaryCategory)

    @Delete
    fun deletePrimaryCategory(primaryCategory: PrimaryCategory)
}