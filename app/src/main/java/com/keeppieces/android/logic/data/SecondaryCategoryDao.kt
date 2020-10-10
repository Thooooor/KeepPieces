package com.keeppieces.android.logic.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SecondaryCategoryDao {
    @Insert
    fun insertSecondaryCategory(secondaryCategory: SecondaryCategory)

    @Update
    fun updateSecondaryCategory(newSecondaryCategory: SecondaryCategory)

    @Delete
    fun deleteSecondaryCategory(secondaryCategory: SecondaryCategory)

    @Query("SELECT * FROM secondaryCategory")
    fun getSecondaryCategory(): LiveData<List<SecondaryCategory>>
}