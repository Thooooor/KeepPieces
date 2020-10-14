package com.keeppieces.android.logic.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PrimaryCategoryDao {
    @Insert
    fun insertPrimaryCategory(primaryCategory: PrimaryCategory)

    @Update
    fun updatePrimaryCategory(newPrimaryCategory: PrimaryCategory)

    @Delete
    fun deletePrimaryCategory(primaryCategory: PrimaryCategory)

    @Query("SELECT * FROM primaryCategory")
    fun getPrimaryCategory(): LiveData<List<PrimaryCategory>>
}