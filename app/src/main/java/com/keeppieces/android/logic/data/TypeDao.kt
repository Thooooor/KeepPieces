package com.keeppieces.android.logic.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TypeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertType(type: Type)

    @Update
    fun updateType(newType: Type)

    @Delete
    fun deleteType(type: Type)

    @Query("SELECT * FROM type")
    fun getType(): LiveData<List<Type>>
}