package com.keeppieces.android.ui.overview

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.logic.data.BillRepository
import java.time.LocalDate


class HomepageSummaryViewModel:ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getNowMonthBillList(date: LocalDate) = BillRepository().getNowMonthBillList(date)
    //这里的接口都要确认
    fun getTodayBillList(date: LocalDate) = BillRepository().getOneDayBill(date.toString())

    fun getTodaySummary(bills: List<Bill>, color: String) = BillRepository().getOneDaySummary(bills, color)  // bills 今日账单

}