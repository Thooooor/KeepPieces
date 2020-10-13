package com.keeppieces.android.ui.daily

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.logic.data.BillRepository
import com.keeppieces.android.logic.data.PrimaryCategoryRepository

class DailyViewModel @ViewModelInject internal constructor(): ViewModel() {
    fun billList(date: String) = BillRepository().getOneDayBill(date)

    fun dailyOverview(bills: List<Bill>, color: String) = BillRepository().getOneDayOverview(bills, color)

    fun dailyPrimaryList(bills: List<Bill>, color: String) = PrimaryCategoryRepository().getDailyPrimaryList(bills, color)
}
