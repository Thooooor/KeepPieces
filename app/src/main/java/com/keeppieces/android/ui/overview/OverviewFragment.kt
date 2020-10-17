package com.keeppieces.android.ui.overview

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.keeppieces.android.R
import com.keeppieces.android.extension.getItemDecoration
import com.keeppieces.android.extension.toCHINADFormatted
import com.keeppieces.android.logic.data.*
import com.keeppieces.android.ui.overview.homepage_card_adapter.AccountSummaryCardAdapter
import com.keeppieces.android.ui.overview.homepage_card_adapter.TodaySummaryCardAdapter
import com.keeppieces.line_indicator.data.LineIndicatorData
import com.keeppieces.line_indicator.data.LineIndicatorPortion
import com.keeppieces.pie_chart.PieAnimation
import com.keeppieces.pie_chart.PieData
import com.keeppieces.pie_chart.PiePortion
import kotlinx.android.synthetic.main.layout_account_summary_card.*
import kotlinx.android.synthetic.main.layout_month_summary_card.*
import kotlinx.android.synthetic.main.layout_today_summary_card.*
import java.time.LocalDate


class OverviewFragment : Fragment() {
    private val viewModel by lazy { ViewModelProvider(this).get(HomepageSummaryViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpView() {
        // viewModel.billList(date)返回一个LiveData<List<Bill>> 注意observe传入的是   LiveData<T>中的这个T
        val todayDate = LocalDate.now()
        val nowMonth = todayDate.monthValue
        val nowYear = todayDate.year
        val firstMonthDate = LocalDate.parse("$nowYear-$nowMonth-01")
        val lastMonthDate = firstMonthDate.plusMonths(1).minusDays(1)
        viewModel.getAllBill().observe(viewLifecycleOwner) { billList ->
//            val allBillList = if (billList.isEmpty()) tempList else billList
            val allBillList = tempList  // 数据库里好像加入了2020/10/15这条数据...
            // 不从数据库中获取数据，对全表数据进行处理
            val nowMonthBillList = viewModel.getPeriodBillWithoutDao(firstMonthDate.toString(),lastMonthDate.toString(),allBillList)
            val todayBillList = viewModel.getPeriodBillWithoutDao(todayDate.toString(),todayDate.toString(),nowMonthBillList)
            setUpMonthSummaryCardView(nowMonthBillList)
            setUpTodaySummaryCardView(todayBillList)
        }
        viewModel.getAllAccount().observe(viewLifecycleOwner) { accountList ->
            val allAccountList = if(accountList.isEmpty()) tempAcountList else tempAcountList
            setUpAccountSummaryCardView(allAccountList)
        }
    }

    private fun setUpMonthSummaryCardView(bills: List<Bill>) {  // bills：这个月的账单表
        var monthIncome:Double = 0.00
        var monthExpenditure:Double = 0.00
        for(bill in bills) {
            when(bill.type) {
                "收入" -> monthIncome += bill.amount
                else -> monthExpenditure += bill.amount
            }
        }
        val piePortions = listOf<PiePortion>(
            PiePortion("支出",monthExpenditure, ContextCompat.getColor(requireContext(), R.color.green_800)),
            PiePortion("收入",monthIncome,ContextCompat.getColor(requireContext(), R.color.green_600)))
        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(month_income_expenditure_pie).apply {
            duration = 600
        }
        month_income_expenditure_pie.setPieData(pieData = pieData, animation = pieAnimation)
        month_income.text = monthIncome.toCHINADFormatted()
        month_expenditure.text = monthExpenditure.toCHINADFormatted()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpTodaySummaryCardView(bills: List<Bill>) {  // bills：今天的账单表
        val todaySummary = viewModel.getTodaySummary(bills, "blue")
        val lineIndicatorData= getLineIndicatorData(todaySummary.bills,
            ::getGeneralBillSecondaryCategory,::getGeneralBillAmount,::getGeneralBillColorInt)
        today_summary_line_indicator.setData(lineIndicatorData)
        today_amount.text = todaySummary.today_total.toCHINADFormatted()
        bill_today_overview_recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
            adapter = TodaySummaryCardAdapter(requireContext(), todaySummary.bills)
        }
    }

    private fun setUpAccountSummaryCardView(accounts: List<Account>) {  // accounts：数据库中所有的账账户情况
        val accountSummary = viewModel.getAccountSummary(accounts, "purple")
        val lineIndicatorData: LineIndicatorData = getLineIndicatorData(accountSummary.accounts,
        ::getDailyAccountAccount,::getDailyAccountAmount,::getDailyAccountColorInt)
        account_summary_line_indicator.setData(lineIndicatorData)
        account_amount.text = accountSummary.total.toCHINADFormatted()
        bill_account_overview_recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
            adapter = AccountSummaryCardAdapter(requireContext(), accountSummary.accounts)
        }
    }

    private fun <T> getLineIndicatorData(dataList:List<T>, getName: (T) -> String, getValue: (T) -> Double, getValueColor:(T)->Int): LineIndicatorData{
        val portions = dataList.map {
            LineIndicatorPortion(
                name = getName(it),
                value = getValue(it).toFloat(),
                colorInt = ContextCompat.getColor(requireContext(), getValueColor(it))
            )
        }
        return LineIndicatorData(portions = portions)
    }

    companion object {
        const val TAG = "OverviewFragment"
        private const val KEY_DAY = "key-day"

        @RequiresApi(Build.VERSION_CODES.O)
        val tempList = listOf(
            Bill(
                date = LocalDate.now().toString(),
                amount = 6.60,
                account = "校园卡",
                member = "Me",
                primaryCategory = "Food",
                secondaryCategory = "Breakfast",
                type = "支出"
            ),
            Bill(
                date = LocalDate.now().toString(),
                amount = 23.60,
                account = "Wechat",
                member = "Me",
                primaryCategory = "Food",
                secondaryCategory = "Lunch",
                type = "支出"
            ),
            Bill(
                date = LocalDate.now().toString(),
                amount = 47.09,
                account = "校园卡",
                member = "Me",
                primaryCategory = "Food",
                secondaryCategory = "Dinner",
                type = "支出"
            ),
            Bill(
                date = LocalDate.now().toString(),
                amount =1299.00,
                account = "Alipay",
                member = "Mom",
                primaryCategory = "Wearing",
                secondaryCategory = "Shoes",
                type = "支出"
            ),
            Bill(
                date = LocalDate.now().toString(),
                amount = 229.90,
                account = "Cash",
                member = "boy friend",
                primaryCategory = "人情",
                secondaryCategory = "礼物",
                type = "支出"
            ),
            Bill(
                date = LocalDate.now().toString(),
                amount = 999.0,
                account = "Wechat",
                member = "Me",
                primaryCategory = "Wearing",
                secondaryCategory = "Shoes",
                type = "支出"
            ),
            Bill(
                date = LocalDate.now().toString(),
                amount = 520.0,
                account = "Wechat",
                member = "Me",
                primaryCategory = "人情",
                secondaryCategory = "红包",
                type = "收入"
            ),
        )
    }

    val tempAcountList = listOf(
        Account("微信",999.00),
        Account("支付宝",-1230.00),
        Account("校园卡",246.40),
        Account("平安银行卡",1456.13)

    )
}
