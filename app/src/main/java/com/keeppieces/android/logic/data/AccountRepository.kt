package com.keeppieces.android.logic.data

import androidx.annotation.ColorRes
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.keeppieces.android.KeepPiecesApplication
import com.keeppieces.android.logic.Repository


class AccountRepository {
    private val context = KeepPiecesApplication.context
    private val accountDao = AppDatabase.getDatabase(context).accountDao()
    private val repository = Repository

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

    fun getDailyAccountList(bills: List<Bill>, color: String): MutableList<DailyAccount> {
        val accountList = mutableListOf<DailyAccount>()
        for (bill in bills) {
            var isAccountExist = false
            for (account in accountList) {
                if (account.account == bill.account) {
                    if (bill.type == "收入") {
                        account.amount += bill.amount
                    } else {
                        account.amount -= bill.amount
                    }
                    isAccountExist = true
                    break
                } else {
                    continue
                }
            }
            if (!isAccountExist) {
                val primaryIndex = accountList.size
                val primaryColor = repository.getColorInt(color, primaryIndex)
                if (bill.type == "收入") {
                    accountList.add(DailyAccount(bill.account, bill.amount, primaryColor))
                } else {
                    accountList.add(DailyAccount(bill.account, -bill.amount, primaryColor))
                }

            } else {
                continue
            }
        }
        return accountList
    }

    fun getAccountSummary(accounts:List<Account>, color: String ):AccountSummary {
        val accountList = mutableListOf<DailyAccount>()
        var total:Double = 0.00
        for (account in accounts) {
            total += account.amount
            val accountNameIndex = accountList.size
            val accountNameColor = repository.getColorInt(color, accountNameIndex)
            accountList.add(DailyAccount(account.name, account.amount, accountNameColor))
        }
        return AccountSummary(total,accountList)
    }
}
data class AccountSummary(val total:Double ,val accounts:List<DailyAccount>)
data class DailyAccount(
    val account: String,
    var amount: Double,
    @ColorRes val color: Int
)

fun getDailyAccountAccount(dailyAccount: DailyAccount):String = dailyAccount.account
fun getDailyAccountAmount(dailyAccount: DailyAccount):Double = dailyAccount.amount
fun getDailyAccountColorInt(dailyAccount: DailyAccount):Int = dailyAccount.color