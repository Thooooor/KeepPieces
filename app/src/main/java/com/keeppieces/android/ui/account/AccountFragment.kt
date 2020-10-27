package com.keeppieces.android.ui.account

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.keeppieces.android.R
import com.keeppieces.android.extension.getItemDecoration
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.logic.data.DailyAccount
import com.keeppieces.android.ui.account.adapter.AccountOverviewAdapter
import com.keeppieces.android.ui.monthly.CustomMode
import com.keeppieces.android.ui.monthly.MonthMode
import com.keeppieces.pie_chart.PieAnimation
import com.keeppieces.pie_chart.PieData
import com.keeppieces.pie_chart.PiePortion
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_monthly.monthlyLeftArrow
import kotlinx.android.synthetic.main.fragment_monthly.monthlyRightArrow
import kotlinx.android.synthetic.main.fragment_monthly.pieChart
import kotlinx.android.synthetic.main.fragment_primary.*
import kotlinx.android.synthetic.main.fragment_primary.timeSpanView
import kotlinx.android.synthetic.main.layout_daily_account_overview.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.chrono.IsoChronology
import kotlin.math.abs


class AccountFragment(var startDate: String, var endDate: String): Fragment() {
    private val viewModel: AccountViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O) var startLocalDate: LocalDate = LocalDate.now()
    @RequiresApi(Build.VERSION_CODES.O) var endLocalDate: LocalDate = LocalDate.now()
    private var timeSpan: Int = 1
    private var cnt: Int = -1
    private var mode = MonthMode
//    lateinit var primaryClassification: MutableMap<String,Pair<List<Bill>,Int>>
    private lateinit var accountSummary : MutableList<AccountDetail>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initDate() {
        startLocalDate = LocalDate.parse(startDate)
        endLocalDate = LocalDate.parse(endDate)
        timeSpan = endLocalDate.dayOfYear - startLocalDate.dayOfYear + 1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateDate(span: Int) {
        if (mode == MonthMode) {
            val monthSpan = if (span > 0) 1 else -1
            startLocalDate = startLocalDate.plusMonths(monthSpan.toLong())
            endLocalDate = startLocalDate.plusMonths(1).plusDays(-1)
        } else {
            startLocalDate = startLocalDate.plusDays(span.toLong())
            endLocalDate = endLocalDate.plusDays(span.toLong())
        }

        startDate = startLocalDate.toString()
        endDate = endLocalDate.toString()
        Log.d("Monthly Date Update", "$startDate ~ $endDate")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateMode() {
        val year = endLocalDate.year
        val month = endLocalDate.monthValue
        val lastDay = endLocalDate.dayOfMonth
        mode = if (startLocalDate.dayOfMonth == 1 && startLocalDate.monthValue == month && lastDay >= 28) {
            when (lastDay) {
                31 -> MonthMode
                30 -> when (month) {
                    4 -> MonthMode
                    6 -> MonthMode
                    9 -> MonthMode
                    11 -> MonthMode
                    else -> CustomMode
                }
                29 -> if (month == 2) MonthMode else CustomMode
                28 -> if (month == 2 && IsoChronology.INSTANCE.isLeapYear(year.toLong())) MonthMode else CustomMode
                else -> CustomMode
            }
        } else {
            CustomMode
        }
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initDate()
        setUpView()

        monthlyLeftArrow.setOnClickListener {
            updateDate(-timeSpan)
            setUpView()
        }
        monthlyRightArrow.setOnClickListener {
            updateDate(timeSpan)
            setUpView()
        }

        timeSpanView.setOnClickListener {
            val builder = MaterialDatePicker.Builder.dateRangePicker()
            val picker = builder.build()
            picker.show(childFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                val format = SimpleDateFormat("yyyy-MM-dd")
                timeSpan = ((it.second!! - it.first!!) / (1000 * 3600 * 24)).toInt() + 1
                timeSpan = if (timeSpan == 0) 1 else timeSpan
                startDate = format.format(it.first)
                startLocalDate = LocalDate.parse(startDate)
                endDate = format.format(it.second)
                endLocalDate = LocalDate.parse(endDate)
                updateMode()
                setUpView()
            }
        }

    }

    //修改
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpView() {
        if (cnt <= 0) cnt++
        viewModel.billList(startDate, endDate).observe(viewLifecycleOwner) { billList ->
            val bills = if (billList.isEmpty()) listOf() else billList
//            accountClassification = viewModel.getAccountClassification(bills)
            accountSummary = viewModel.getAccountSummary(bills, "blue", "yellow")
            timeSpanView.text = StringBuilder("$startDate ~ $endDate").toString()
            setUpPieView()
            setUpAccountCard()
        }
    }

    private fun setUpPieView() {
        val piePortions = accountSummary.map {
            PiePortion(
                it.account, abs(it.lastAmount), ContextCompat.getColor(requireContext(), it.color)
            )// 这里的amount正负由颜色确定
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(pieChart).apply {
            duration = 600
        }
        pieChart.setPieData(pieData = pieData, animation = pieAnimation)
    }

    private fun setUpAccountCard() {
        var inAmount = 0.00
        var outAmount = 0.00
        for (item in accountSummary) {
            inAmount += item.inAmount
            outAmount += item.outAmount
        }
        accountOverviewRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
//            setHasFixedSize(true)
            if (cnt == 0) addItemDecoration(getItemDecoration())
            adapter = AccountOverviewAdapter(accountSummary, inAmount, outAmount)
        }
    }

    private fun setUpAccountPieView(accountList: List<DailyAccount>) {
        dailyAccountTitle.text = "成员"
        val piePortions = accountList.map {
            PiePortion(
                it.account, it.amount, ContextCompat.getColor(requireContext(), it.color)
            )
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(dailyAccountOverviewPie).apply {
            duration = 600
        }

        dailyAccountOverviewPie.setPieData(pieData = pieData, animation = pieAnimation)
    }

    companion object {
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
        )
    }
}