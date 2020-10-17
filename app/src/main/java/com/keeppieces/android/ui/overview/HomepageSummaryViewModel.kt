package com.keeppieces.android.ui.overview

import androidx.lifecycle.ViewModel
import com.keeppieces.android.logic.data.Account
import com.keeppieces.android.logic.data.AccountRepository
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.logic.data.BillRepository


class HomepageSummaryViewModel:ViewModel() {
    private val billRepository = BillRepository()
    private val accountRepository = AccountRepository()
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getNowMonthBillList(date: LocalDate) = billRepository.getNowMonthBillList(date)
//    fun getTodayBillList(date: LocalDate) = billRepository.getOneDayBill(date.toString())

    fun getTodaySummary(oneDayBills: List<Bill>, color: String) = billRepository.getOneDaySummary(oneDayBills, color)  // bills 今日账单

    fun getAllBill() = billRepository.getAllBill()

    // 从 bills 挑选出 符合时间区间条件的 所有账单
    fun getPeriodBillWithoutDao(startDate:String, endDate:String, bills: List<Bill>) = billRepository.getPeriodBillWithoutDao(startDate,endDate,bills)

    fun getAllAccount() = accountRepository.getAccount()


    fun getAccountSummary(allAccounts:List<Account>, color:String) =  accountRepository.getAccountSummary(allAccounts, color)

}