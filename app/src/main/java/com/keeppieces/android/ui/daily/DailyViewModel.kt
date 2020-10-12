package com.keeppieces.android.ui.daily

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.logic.data.BillRepository

class DailyViewModel @ViewModelInject internal constructor(): ViewModel() {
    fun billList(date: String) = BillRepository().getOneDayBill(date)

    fun dailyOverview(bills: List<Bill>) = BillRepository().getOneDayOverview(bills)
}
