package com.keeppieces.android.ui.monthly

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.keeppieces.android.logic.data.*

class MonthlyViewModel @ViewModelInject internal constructor(): ViewModel() {
    fun billList(startDate: String, endDate: String) = BillRepository().getPeriodBill(startDate, endDate)
    fun monthlyOverview(bills: List<Bill>, color: String) = BillRepository().getOneDayOverview(bills, color)

    fun monthlyPrimaryList(bills: List<Bill>, color: String) = PrimaryCategoryRepository().getDailyPrimaryList(bills, color)

    fun monthlyAccountList(bills: List<Bill>, color: String) = AccountRepository().getDailyAccountList(bills, color)

    fun monthlyMemberList(bills: List<Bill>, color: String) = MemberRepository().getDailyMemberList(bills, color)

    fun monthlyTypeList(bills: List<Bill>) = TypeRepository().getDailyTypeList(bills)
}