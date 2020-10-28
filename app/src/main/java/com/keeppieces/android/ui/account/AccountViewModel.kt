package com.keeppieces.android.ui.account

import androidx.annotation.ColorRes
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.keeppieces.android.logic.data.*

class AccountViewModel @ViewModelInject internal constructor(): ViewModel() {

    fun billList(startDate: String, endDate: String) = BillRepository().getPeriodBill(startDate, endDate)

    fun monthlyOverview(bills: List<Bill>, color: String) = BillRepository().getOneDayOverview(bills, color)

    fun monthlyPrimaryList(bills: List<Bill>, color: String) = PrimaryCategoryRepository().getDailyPrimaryList(bills, color)

    fun monthlyAccountList(bills: List<Bill>, color: String) = AccountRepository().getDailyAccountList(bills, color)

    fun monthlyMemberList(bills: List<Bill>, color: String) = AccountRepository().getDailyAccountList(bills, color)

    fun monthlyTypeList(bills: List<Bill>) = TypeRepository().getDailyTypeList(bills)

    fun getAccountClassification(bills:List<Bill>) = AccountRepository().getAccountClassification(bills)

    fun getAccountPeriodList(startDate: String, endDate: String, account: String) = BillRepository().getAccountPeriodBill(startDate, endDate, account)

    fun getAccountSummary(bills: List<Bill>, positiveColor: String, negativeColor: String) : MutableList<AccountDetail> {
        return AccountRepository().getAccountSummaryInAccount(bills, positiveColor, negativeColor)
    }
}

data class AccountDetail(
    val account: String,
    var inAmount: Double,
    var outAmount: Double,
    var lastAmount : Double,
//    var finalAmount : Double,
    @ColorRes val color: Int,
    var outMaxCategory : String,
    var inMaxCategory : String,
    var outMaxMember: String,
    var inMaxMember: String
)

//data class AccountSummary(val total: Double, val accounts: List<AccountDetail>)
data class DailyOverview(val total: Double, val bills: List<GeneralBill>)