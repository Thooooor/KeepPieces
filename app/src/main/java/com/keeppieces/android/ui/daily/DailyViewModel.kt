package com.keeppieces.android.ui.daily

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.keeppieces.android.logic.data.*

class DailyViewModel @ViewModelInject internal constructor(): ViewModel() {
    fun billList(date: String) = BillRepository().getOneDayBill(date)

    fun dailyOverview(bills: List<Bill>, color: String) = BillRepository().getOneDayOverview(bills, color)

    fun dailyPrimaryList(bills: List<Bill>, color: String) = PrimaryCategoryRepository().getDailyPrimaryList(bills, color)

    fun dailyAccountList(bills: List<Bill>, color: String) = AccountRepository().getDailyAccountList(bills, color)

    fun dailyMemberList(bills: List<Bill>, color: String) = MemberRepository().getDailyMemberList(bills, color)

    fun dailyTypeList(bills: List<Bill>) = TypeRepository().getDailyTypeList(bills)
}
