package com.keeppieces.android.logic.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface AccountDao {
    @Insert
    fun insertAccount(account: Account): Long

    @Update
    fun updateAccount(newAccount: Account)

    @Delete
    fun deleteAccount(account: Account)
}