package com.keeppieces.android.logic.data

import androidx.annotation.ColorRes
import com.keeppieces.android.KeepPiecesApplication
import com.keeppieces.android.logic.Repository
import com.keeppieces.android.ui.account.AccountDetail
import kotlin.math.abs


class AccountRepository {
    private val context = KeepPiecesApplication.context
    private val accountDao = AppDatabase.getDatabase(context).accountDao()
    private val repository = Repository

    fun createAccount(account: Account) {
        accountDao.insertAccount(account)
    }

    fun getAccount() = accountDao.getAccount()

    fun getAAccount(accountName: String) = accountDao.getAAccount(accountName)

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

    fun getAccountSummary(accountList: MutableMap<String, Pair<List<Bill>, Int>>, positiveColor: String, negativeColor: String): MutableList<DailyAccount> {
//        val accountList = mutableListOf<DailyAccount>()
//        var total = 0.00
//        var positiveSize = 0
//        var negativeSize = 0
//        for (account in accounts) {
//            total += account.amount
//            if(account.amount>=0) {
//                val accountNameColor = repository.getColorInt(positiveColor, positiveSize)
//                positiveSize += 1
//                accountList.add(DailyAccount(account.name, account.amount, accountNameColor))
//            }
//            else {
//                val accountNameColor = repository.getColorInt(negativeColor, negativeSize)
//                negativeSize += 1
//                accountList.add(DailyAccount(account.name, account.amount, accountNameColor))
//            }
//        }
//        accountList.sortBy {it.amount }
//        return AccountSummary(total,accountList)
        val accountMonthCondition = mutableMapOf<String, Pair<Double, Int>>()
        val accountSummaryList = mutableListOf<DailyAccount>()
        for (accountBill in accountList) {
            var amount = 0.00
            for (bill in accountBill.value.first) {
                if (bill.type == "收入" || (bill.type == "转账" && bill.secondaryCategory == accountBill.key)) {
                    amount += bill.amount
                } else if (bill.type == "支出" || (bill.type == "转账" && bill.account == accountBill.key)) {
                    amount -= bill.amount
                }
            }
            val accountColor = repository.getColorInt(
                if (amount>=0) {positiveColor} else {negativeColor}, accountBill.value.second)
            accountMonthCondition[accountBill.key] = Pair(amount, accountColor)
        }
        accountMonthCondition.forEach { (account, condition) ->
            accountSummaryList.add(DailyAccount(account, condition.first, condition.second))
        }
        accountSummaryList.sortByDescending { abs(it.amount) }
        return accountSummaryList
    }

    fun getAccountClassification(bills: List<Bill>): MutableMap<String, Pair<List<Bill>, Int>> {
        val accountClassification = mutableMapOf<String,Pair<List<Bill>, Int>>()
        var count = 0
        for (bill in bills) {
            if (bill.account !in accountClassification) {
                accountClassification[bill.account] = Pair(mutableListOf(bill), count++)
            } else if (bill.type != "转账"){
                accountClassification[bill.account] = Pair(
                    accountClassification[bill.account]!!.first.plus(bill),
                    accountClassification[bill.account]!!.second)
            } else {
                if (bill.secondaryCategory !in accountClassification) {
                    accountClassification[bill.secondaryCategory] = Pair(mutableListOf(), count++)
                }
                accountClassification[bill.account] = Pair(
                    accountClassification[bill.account]!!.first.plus(bill),
                    accountClassification[bill.account]!!.second)
                accountClassification[bill.secondaryCategory] = Pair(
                    accountClassification[bill.secondaryCategory]!!.first.plus(bill),
                    accountClassification[bill.secondaryCategory]!!.second)
            }
        }
        return accountClassification
    }

    fun getAccountSummaryInAccount(
        bills: List<Bill>,
        positiveColor: String,
        negativeColor: String
    ): MutableList<AccountDetail> {
        val accountList = mutableListOf<AccountDetail>()
        val accountClassification = getAccountClassification(bills)
        for (accountBill in accountClassification) {
            var inAmount = 0.00
            var outAmount = 0.00
            val outCategoryAmount = HashMap<String, Double>()
            val inCategoryAmount = HashMap<String, Double>()
            val outMemberAmount = HashMap<String, Double>()
            val inMemberAmount = HashMap<String, Double>()
            for (bill in accountBill.value.first) {
                if (bill.type == "收入" || (bill.type == "转账" && bill.secondaryCategory == accountBill.key)) {
                    inAmount += bill.amount
                    if (bill.primaryCategory !in inCategoryAmount) {
                        inCategoryAmount[bill.primaryCategory] = bill.amount
                    } else {
                        inCategoryAmount[bill.primaryCategory] = inCategoryAmount[bill.primaryCategory]!! + bill.amount
                    }
                    if (bill.member !in inMemberAmount) {
                        inMemberAmount[bill.member] = bill.amount
                    } else {
                        inMemberAmount[bill.member] = inMemberAmount[bill.member]!! + bill.amount
                    }
                } else if (bill.type == "支出" || (bill.type == "转账" && bill.account == accountBill.key)) {
                    outAmount += bill.amount
                    if (bill.primaryCategory !in outCategoryAmount) {
                        outCategoryAmount[bill.primaryCategory] = bill.amount
                    } else {
                        outCategoryAmount[bill.primaryCategory] = outCategoryAmount[bill.primaryCategory]!! + bill.amount
                    }
                    if (bill.member !in outMemberAmount) {
                        outMemberAmount[bill.member] = bill.amount
                    } else {
                        outMemberAmount[bill.member] = outMemberAmount[bill.member]!! + bill.amount
                    }
                }
            }
            val accountColor = repository.getColorInt(
                if (inAmount-outAmount>=0) {positiveColor} else {negativeColor}, accountBill.value.second)
            val inMaxCategory = getMapMax(inCategoryAmount)
            val outMaxCategory = getMapMax(outCategoryAmount)
            val inMaxMember = getMapMax(inMemberAmount)
            val outMaxMember = getMapMax(outMemberAmount)
            accountList.add(AccountDetail(accountBill.key,
                inAmount,
                outAmount,
                inAmount-outAmount,
                accountColor,
                outMaxCategory,
                inMaxCategory,
                outMaxMember,
                inMaxMember))
        }
        return accountList
    }

    private fun getMapMax(map: HashMap<String, Double>) : String {
        var maxValue = 0.00
        var maxText = ""
        for (item in map) {
            if (item.value > maxValue) {
                maxValue = item.value
                maxText = item.key
            }
        }
        return maxText
    }
}

data class AccountSummary(val total:Double ,val accounts:List<DailyAccount>)
data class DailyAccount(
    val account: String,
    var amount: Double,
    @ColorRes val color: Int
)

fun getDailyAccountAccount(dailyAccount: DailyAccount):String = dailyAccount.account
fun getDailyAccountAmount(dailyAccount: DailyAccount):Double = abs(dailyAccount.amount)
fun getDailyAccountColorInt(dailyAccount: DailyAccount):Int = dailyAccount.color