@file:Suppress("UNCHECKED_CAST")

package com.keeppieces.android.ui.overview

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.keeppieces.android.MainActivity
import com.keeppieces.android.R
import com.keeppieces.android.extension.getItemDecoration
import com.keeppieces.android.extension.toCHINADFormatted
import com.keeppieces.android.logic.data.*
import com.keeppieces.android.ui.bill.BillActivity
import com.keeppieces.android.ui.blank.AccountPage
import com.keeppieces.android.ui.blank.MemberPage
import com.keeppieces.android.ui.blank.currentType
import com.keeppieces.android.ui.overview.homepage_card_adapter.AccountSummaryCardAdapter
import com.keeppieces.android.ui.overview.homepage_card_adapter.MemberSummaryCardAdapter
import com.keeppieces.android.ui.overview.homepage_card_adapter.TodaySummaryCardAdapter
import com.keeppieces.line_indicator.data.LineIndicatorData
import com.keeppieces.line_indicator.data.LineIndicatorPortion
import com.keeppieces.pie_chart.PieAnimation
import com.keeppieces.pie_chart.PieData
import com.keeppieces.pie_chart.PiePortion
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.layout_account_summary_card.*
import kotlinx.android.synthetic.main.layout_member_summary_card.*
import kotlinx.android.synthetic.main.layout_month_summary_card.*
import kotlinx.android.synthetic.main.layout_today_summary_card.*
import java.time.LocalDate
import kotlin.math.abs
import kotlin.properties.Delegates


class OverviewFragment : Fragment() {
    private val viewModel by lazy { ViewModelProvider(this).get(HomepageSummaryViewModel::class.java) }
    private val addMonthBudgetDialog = AddMonthBudgetDialog()

    private val monthBudgetFile = "month_budget"
    private val nowMonthBudgetString = "nowMonthBudget"
    private val nowMonthString = "nowMonth"
    private val nowYearString = "nowYear"
    private var savedMonthBudget:String? = null
    var savedYear by Delegates.notNull<Int>()
    var savedMonth by Delegates.notNull<Int>()

    @RequiresApi(Build.VERSION_CODES.O)
    val nowDate: LocalDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    val nowMonth = nowDate.monthValue

    @RequiresApi(Build.VERSION_CODES.O)
    val nowYear = nowDate.year

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view:View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentManager = parentFragmentManager
        val prefs: SharedPreferences = requireActivity().getSharedPreferences(
            monthBudgetFile,
            Context.MODE_PRIVATE
        )
        savedYear = prefs.getInt(nowYearString, -1)
        savedMonth = prefs.getInt(nowMonthString, -1)
        savedMonthBudget = prefs.getString(nowMonthBudgetString, null)
        if (savedYear == nowYear && savedMonth == nowMonth && savedMonthBudget != null) {
            set_month_budget.text = savedMonthBudget!!.toDouble().toCHINADFormatted()
            used_budget_percent.text = "0.0%"
        }
        addFab.setOnClickListener {
            BillActivity.start(requireContext())
        }
        today_see_more.setOnClickListener {
            getParentActivity<MainActivity>().view_pager.setCurrentItem(1, true)
        }
        account_see_more.setOnClickListener {
            currentType = AccountPage
            getParentActivity<MainActivity>().view_pager.setCurrentItem(3, true)
        }
        member_see_more.setOnClickListener {
            currentType = MemberPage
            getParentActivity<MainActivity>().view_pager.setCurrentItem(3, true)
        }
        set_month_budget.setOnClickListener {
            addMonthBudgetDialog.show(fragmentManager, "addMonthBudget")
        }
        setUpCardView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpCardView() {
        val todayDate = LocalDate.now()
        val nowMonth = todayDate.monthValue
        val nowYear = todayDate.year
        val firstMonthDate = LocalDate.parse("$nowYear-$nowMonth-01")
        val lastMonthDate = firstMonthDate.plusMonths(1).minusDays(1)

        // 更新并展示本月、本日概要卡片
        viewModel.allBillLiveData.observe(viewLifecycleOwner) { billList ->
            // 不从数据库中获取数据，对全表数据进行处理
            val nowMonthBillList = viewModel.getPeriodBillWithoutDao(
                firstMonthDate.toString(), lastMonthDate.toString(), billList
            )
            val todayBillList = viewModel.getPeriodBillWithoutDao(
                todayDate.toString(), todayDate.toString(), nowMonthBillList
            )
            setUpMonthSummaryCardView(nowMonthBillList)
            setUpTodaySummaryCardView(todayBillList)
            setUpMemberMonthSummaryCardView(nowMonthBillList)
            setUpAccountSummaryCardView(nowMonthBillList)
        }
//
//        // 更新并展示账户卡片
//        viewModel.allAccountLiveData.observe(viewLifecycleOwner) { accountList ->
//
//        }

    }

    private fun setUpMonthSummaryCardView(bills: List<Bill>) {  // bills：这个月的账单表
        var monthIncome = 0.00
        var monthExpenditure = 0.00
        for (bill in bills) {
            when (bill.type) {
                "收入" -> monthIncome += bill.amount
                "支出" -> monthExpenditure += bill.amount
            }
        }
        val piePortions = listOf(
            PiePortion(
                "支出",
                monthExpenditure,
                ContextCompat.getColor(requireContext(), R.color.green_800)
            ),
            PiePortion(
                "收入",
                monthIncome,
                ContextCompat.getColor(requireContext(), R.color.green_600)
            )
        )
        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(month_income_expenditure_pie).apply {
            duration = 600
        }
        month_income_expenditure_pie.setPieData(pieData = pieData, animation = pieAnimation)
        month_income.text = monthIncome.toCHINADFormatted()
        month_expenditure.text = monthExpenditure.toCHINADFormatted()
        if (savedYear == nowYear && savedMonth == nowMonth && savedMonthBudget != null) {
            val string = String.format("%.1f%%",(100*monthExpenditure/ savedMonthBudget!!.toDouble()))
            used_budget_percent.text = string
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpTodaySummaryCardView(bills: List<Bill>) {  // bills：今天的账单表
        val todaySummary = viewModel.getTodaySummary(bills, "blue")
        Log.d("todaySummary.Bill",todaySummary.bills.size.toString())
        val lineIndicatorData = getLineIndicatorData(
            requireContext(),
            todaySummary.bills,
            ::getGeneralBillSecondaryCategory, ::getGeneralBillAmount, ::getGeneralBillColorInt
        )
        today_summary_line_indicator.setData(lineIndicatorData)
        today_amount.text = todaySummary.today_total.toCHINADFormatted()
        bill_today_overview_recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
            adapter = TodaySummaryCardAdapter(requireContext(), todaySummary.bills)
        }
    }

    private fun setUpAccountSummaryCardView(bills: MutableList<Bill>) {  // accounts：数据库中所有的账账户情况
        val accountListAbout = viewModel.getAccountClassification(bills)
        val accountSummary = viewModel.getAccountSummary(accountListAbout, "green", "purple")
        val lineIndicatorData = getLineIndicatorData(
            this.requireContext(),
            accountSummary,
            ::getDailyAccountAccount, ::getDailyAccountAmount, ::getDailyAccountColorInt
        )
        var amount = 0.00
        for (item in accountSummary) {
            amount += item.amount
        }
        account_summary_line_indicator.setData(lineIndicatorData)
        account_amount.text = amount.toCHINADFormatted()
        bill_account_overview_recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
            adapter = AccountSummaryCardAdapter(requireContext(), accountSummary)
        }
//        account_summary_line_indicator.setData(lineIndicatorData)
//        account_amount.text = accountSummary.total.toCHINADFormatted()
//        bill_account_overview_recyclerview.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            setHasFixedSize(true)
//            addItemDecoration(getItemDecoration())
//            adapter = AccountSummaryCardAdapter(requireContext(), accountSummary.accounts)
//        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setUpMemberMonthSummaryCardView(monthBillList: List<Bill>) {
        val memberMonthSummary = viewModel.getMemberMonthSummary(monthBillList, "yellow")
        val lineIndicatorData = getLineIndicatorData(
            requireActivity().applicationContext,
            memberMonthSummary,
            ::getDailyMemberMember, ::getDailyMemberAmount, ::getDailyMemberColorInt
        )
        member_month_summary_line_indicator.setData(lineIndicatorData)
        bill_member_overview_recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
            adapter = MemberSummaryCardAdapter(requireContext(), memberMonthSummary)
        }
    }

    private fun <T> getLineIndicatorData(
        context: Context,
        dataList: List<T>,
        getName: (T) -> String,
        getValue: (T) -> Double,
        getValueColor: (T) -> Int
    ): LineIndicatorData {
        Log.d(TAG, "check point")
        val portions = dataList.map {
            LineIndicatorPortion(
                name = getName(it),
                value = abs(getValue(it).toFloat()),
                colorInt = ContextCompat.getColor(context,getValueColor(it))
            )
        }.toList()
        return LineIndicatorData(portions = portions)
    }

    companion object {
        const val TAG = "OverviewFragment"
    }
}

fun <T : AppCompatActivity> Fragment.getParentActivity(): T {
    return requireActivity() as T
}