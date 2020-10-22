package com.keeppieces.android.logic.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAccount(account: Account)

    @Update
    fun updateAccount(newAccount: Account)

    @Delete
    fun deleteAccount(account: Account)

    @Query("SELECT * FROM account ORDER BY amount DESC")
    fun getAccount(): LiveData<List<Account>>
}