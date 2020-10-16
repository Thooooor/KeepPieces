package com.keeppieces.android.ui.overview

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.keeppieces.android.R
import com.keeppieces.android.extension.getItemDecoration
import com.keeppieces.android.extension.toCHINADFormatted
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.ui.daily.DailyFragment
import com.keeppieces.android.ui.daily.adapter.DailyPrimaryOverviewAdapter
import com.keeppieces.android.ui.overview.homepage_card_adapter.TodaySummaryCardAdapter
import com.keeppieces.pie_chart.PieAnimation
import com.keeppieces.pie_chart.PieData
import com.keeppieces.pie_chart.PiePortion
import kotlinx.android.synthetic.main.layout_daily_primary_overview.*
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
        Log.d(OverviewFragment.TAG, "onActivityCreated")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpView() {
        // viewModel.billList(date)返回一个LiveData<List<Bill>> 注意observe传入的是   LiveData<T>中的这个T
        viewModel.getNowMonthBillList(LocalDate.now()).observe(viewLifecycleOwner) { billList ->
            val nowMonthBillList = if (billList.isEmpty()) tempList else billList
            setUpMonthSummaryCardView(nowMonthBillList)
//            setUpPrimaryCard(bills)
//            setUpAccountCard(bills)
//            setUpMemberCard(bills)
        }
        viewModel.getTodayBillList(LocalDate.now()).observe(viewLifecycleOwner) { billList ->
            val todayBillList = if (billList.isEmpty()) tempList else billList
            setUpTodaySummaryCardView(todayBillList)
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
            PiePortion("支出",monthExpenditure,R.color.orange_300),
            PiePortion("收入",monthIncome,R.color.green_300))
        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(month_income_expenditure_pie).apply {
            duration = 600
        }
        month_income_expenditure_pie.setPieData(pieData = pieData, animation = pieAnimation)
        month_income.text = monthIncome.toCHINADFormatted().toString()
        month_expenditure.text = monthExpenditure.toCHINADFormatted().toString()
    }

    private fun setUpTodaySummaryCardView(bills: List<Bill>) {  // bills：今天的账单表
        val todaySummary = viewModel.todaySummary(bills,"blue")
        today_amount.text = todaySummary.today_total.toCHINADFormatted()
        bill_today_overview_recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
            adapter = TodaySummaryCardAdapter(requireContext(),todaySummary.bills)
        }
    }

    companion object {
        const val TAG = "OverviewFragment"
        private const val KEY_DAY = "key-day"
        fun newInstance(day: Int): DailyFragment {
            return DailyFragment().apply {
                arguments = Bundle().apply { putInt(KEY_DAY, day) }
            }
        }

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
}
