package com.keeppieces.android.logic.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AccountDao {
    @Insert
    fun insertAccount(account: Account)

    @Update
    fun updateAccount(newAccount: Account)

    @Delete
    fun deleteAccount(account: Account)

    @Query("SELECT * FROM account")
    fun getAccount(): LiveData<List<Account>>
}