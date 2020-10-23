package com.keeppieces.android.ui.monthly

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
import com.keeppieces.android.extension.toCHINADFormatted
import com.keeppieces.android.logic.data.*
import com.keeppieces.android.ui.detail.DetailActivity
import com.keeppieces.android.ui.monthly.adapter.MonthlyAccountOverviewAdapter
import com.keeppieces.android.ui.monthly.adapter.MonthlyMemberOverviewAdapter
import com.keeppieces.android.ui.monthly.adapter.MonthlyPrimaryOverviewAdapter
import com.keeppieces.android.ui.monthly.adapter.MonthlyTypeOverviewAdapter
import com.keeppieces.pie_chart.PieAnimation
import com.keeppieces.pie_chart.PieData
import com.keeppieces.pie_chart.PiePortion
import kotlinx.android.synthetic.main.fragment_monthly.*
import kotlinx.android.synthetic.main.layout_daily_account_overview.*
import kotlinx.android.synthetic.main.layout_daily_member_overview.*
import kotlinx.android.synthetic.main.layout_daily_primary_overview.*
import kotlinx.android.synthetic.main.layout_daily_type_overview.*
import java.text.SimpleDateFormat
import java.time.LocalDate

class MonthlyFragment(var startDate: String, var endDate: String): Fragment() {
    private val viewModel: MonthlyViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O) var startLocalDate: LocalDate = LocalDate.now()
    @RequiresApi(Build.VERSION_CODES.O) var endLocalDate: LocalDate = LocalDate.now()
    private var timeSpan: Int = 1
    private var cnt: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_monthly, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initDate() {
        startLocalDate = LocalDate.parse(startDate)
        endLocalDate = LocalDate.parse(endDate)
        timeSpan = when (endLocalDate.dayOfYear - startLocalDate.dayOfYear) {
            0 ->  1
            else -> endLocalDate.dayOfYear - startLocalDate.dayOfYear
        }
    }

    private fun updateDate(span: Int) {
        startLocalDate = startLocalDate.plusDays(span.toLong())
        endLocalDate = endLocalDate.plusDays(span.toLong())
        startDate = startLocalDate.toString()
        endDate = endLocalDate.toString()
        Log.d("Monthly Date Update", "$startDate ~ $endDate")
    }

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

        labelAlert.setOnClickListener {
            val builder = MaterialDatePicker.Builder.dateRangePicker()
            val picker = builder.build()
            picker.show(childFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                labelAlert.text = picker.headerText.toString()
                val format = SimpleDateFormat("yyyy-MM-dd")
                timeSpan = ((it.second!! - it.first!!) / (1000 * 3600 * 24)).toInt()
                timeSpan = if (timeSpan == 0) 1 else timeSpan
                Log.d("Monthly Date Picker", timeSpan.toString())
                startDate = format.format(it.first)
                startLocalDate = LocalDate.parse(startDate)
                endDate = format.format(it.second)
                endLocalDate = LocalDate.parse(endDate)
                setUpView()
            }
        }

        monthlyDetailBtn.setOnClickListener {
            DetailActivity.start(it.context, LocalDate.now(), LocalDate.now(), R.color.dark_green)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpView() {
        if (cnt <= 0) cnt++
        viewModel.billList(startDate, endDate).observe(viewLifecycleOwner) { billList ->
            val bills = if (billList.isEmpty()) tempList else billList
            labelAlert.text = "$startDate ~ $endDate"
            setUpPieView(bills)
            setUpTypeCard(bills)
            setUpPrimaryCard(bills)
            setUpAccountCard(bills)
            setUpMemberCard(bills)
        }
    }

    private fun setUpPieView(bills: List<Bill>) {
        val monthlyOverview = viewModel.monthlyOverview(bills, "green")
        monthlyAmount.text = monthlyOverview.total.toCHINADFormatted()
        val piePortions = monthlyOverview.bills.map {
            PiePortion(
                it.secondaryCategory, it.amount, ContextCompat.getColor(requireContext(), it.color)
            )
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(pieChart).apply {
            duration = 600
        }
        pieChart.setPieData(pieData = pieData, animation = pieAnimation)
    }

    private fun setUpPrimaryCard(bills: List<Bill>) {
        val primaryList = viewModel.monthlyPrimaryList(bills, "blue")
        dailyPrimaryDetailRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            if (cnt == 0) addItemDecoration(getItemDecoration())
            adapter = MonthlyPrimaryOverviewAdapter(primaryList)
        }
        setUpPrimaryPieView(primaryList)
    }

    private fun setUpPrimaryPieView(primaryList: List<DailyPrimary>) {
        daily_primary_title.text = "分类"
        val piePortions = primaryList.map {
            PiePortion(
                it.primaryCategory, it.amount, ContextCompat.getColor(requireContext(), it.color)
            )
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(dailyPrimaryOverviewPie).apply {
            duration = 600
        }

        dailyPrimaryOverviewPie.setPieData(pieData = pieData, animation = pieAnimation)
    }

    private fun setUpAccountCard(bills: List<Bill>) {
        val accountList = viewModel.monthlyAccountList(bills, "purple")
        dailyAccountDetailRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            if (cnt == 0) addItemDecoration(getItemDecoration())
            adapter = MonthlyAccountOverviewAdapter(accountList)
        }
        setUpAccountPieView(accountList)
    }

    private fun setUpAccountPieView(accountList: List<DailyAccount>) {
        dailyAccountTitle.text = "账户"
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

    private fun setUpMemberCard(bills: List<Bill>) {
        val memberList = viewModel.monthlyMemberList(bills, "orange")
        dailyMemberDetailRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            if (cnt == 0) addItemDecoration(getItemDecoration())
            adapter = MonthlyMemberOverviewAdapter(memberList)
        }
        setUpMemberPieView(memberList)
    }

    private fun setUpMemberPieView(memberList: List<DailyMember>) {
        dailyMemberTitle.text = "成员"
        val piePortions = memberList.map {
            PiePortion(
                it.member, it.amount, ContextCompat.getColor(requireContext(), it.color)
            )
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(dailyMemberOverviewPie).apply {
            duration = 600
        }

        dailyMemberOverviewPie.setPieData(pieData = pieData, animation = pieAnimation)
    }

    private fun setUpTypeCard(bills: List<Bill>) {
        val typeList = viewModel.monthlyTypeList(bills)
        dailyTypeDetailRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            if (cnt == 0) addItemDecoration(getItemDecoration())
            adapter = MonthlyTypeOverviewAdapter(typeList)
        }
        setUpTypePieView(typeList)
    }

    private fun setUpTypePieView(typeList: List<DailyType>) {
        dailyTypeTitle.text = "收支"
        val piePortions = typeList.map {
            PiePortion(
                it.type, it.amount, ContextCompat.getColor(requireContext(), it.color)
            )
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(dailyTypeOverviewPie).apply {
            duration = 600
        }

        dailyTypeOverviewPie.setPieData(pieData = pieData, animation = pieAnimation)
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