package com.keeppieces.android.logic.data

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.keeppieces.android.KeepPiecesApplication


class AccountRepository {
    private val context = KeepPiecesApplication.context
    private val accountDao = AppDatabase.getDatabase(context).accountDao()

    fun createAccount(account: Account) {
        accountDao.insertAccount(account)
    }

    fun getAccount() = accountDao.getAccount()

    fun updateAccount(account: Account) {
        accountDao.updateAccount(account)
    }

    fun deleteAccount(account: Account) {
        accountDao.deleteAccount(account)
    }
}