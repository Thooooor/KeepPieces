package com.keeppieces.android.ui.daily

import android.app.DatePickerDialog
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
import com.keeppieces.android.R
import com.keeppieces.android.extension.getItemDecoration
import com.keeppieces.android.extension.toCHINADFormatted
import com.keeppieces.android.logic.data.*
import com.keeppieces.android.ui.daily.adapter.DailyAccountOverviewAdapter
import com.keeppieces.android.ui.daily.adapter.DailyMemberOverviewAdapter
import com.keeppieces.android.ui.daily.adapter.DailyPrimaryOverviewAdapter
import com.keeppieces.android.ui.daily.adapter.DailyTypeOverviewAdapter
import com.keeppieces.android.ui.detail.DailyDetail
import com.keeppieces.android.ui.detail.DetailActivity
import com.keeppieces.pie_chart.PieAnimation
import com.keeppieces.pie_chart.PieData
import com.keeppieces.pie_chart.PiePortion
import kotlinx.android.synthetic.main.fragment_daily.*
import kotlinx.android.synthetic.main.layout_daily_account_overview.*
import kotlinx.android.synthetic.main.layout_daily_member_overview.*
import kotlinx.android.synthetic.main.layout_daily_primary_overview.*
import kotlinx.android.synthetic.main.layout_daily_type_overview.*
import java.time.LocalDate
import kotlin.math.absoluteValue


class DailyFragment : Fragment() {
    private val viewModel: DailyViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    private var today = LocalDate.now()
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var cnt: Int = -1
    private lateinit var date: String

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initSetting() {
        today = LocalDate.now()
        year = today.year
        month = today.monthValue
        day = today.dayOfMonth
        date = today.toString()
        Log.d("Daily Init", "$year-$month-$day")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateDate() {
        date = today.toString()
        Log.d("Daily Date Update", date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onDatePicker(pickerYear: Int, pickerMonth: Int, pickerDay: Int) {
        year = pickerYear
        month = pickerMonth + 1
        day = pickerDay
        today = if (month >= 10) {
            LocalDate.parse("$year-$month-$day")
        } else {
            LocalDate.parse("$year-0$month-$day")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_daily, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initSetting()
        setUpView()
        dailyLeftArrow.setOnClickListener {
            today = today.plusDays(-1)
            updateDate()
            setUpView()
        }
        dailyRightArrow.setOnClickListener {
            today = today.plusDays(1)
            updateDate()
            setUpView()
        }
        labelAlert.setOnClickListener {
            val datePickerDialog = DatePickerDialog(it.context, { _, year, month, dayOfMonth ->
                onDatePicker(year, month, dayOfMonth)
                updateDate()
                setUpView()
            }, year, month-1, day)
            datePickerDialog.show()
        }
        dailyDetailBtn.setOnClickListener {
            DetailActivity.start(it.context, date, date, DailyDetail, R.color.dark_green)
        }
    }

    private fun setUpView() {
        if (cnt <= 0) cnt++
        viewModel.billList(date).observe(viewLifecycleOwner) { billList ->
            val bills = if (billList.isEmpty()) listOf() else billList
            setUpPieView(bills)
            setUpTypeCard(bills)
            setUpPrimaryCard(bills)
            setUpAccountCard(bills)
            setUpMemberCard(bills)
        }
    }

    private fun setUpPieView(bills: List<Bill>) {
        labelAlert.text = date
        val dailyOverview = viewModel.dailyOverview(bills, "green")
        dailyAmount.text = dailyOverview.total.toCHINADFormatted()
        val piePortions = dailyOverview.bills.map {
            PiePortion(
                it.secondaryCategory, it.amount.absoluteValue, ContextCompat.getColor(requireContext(), it.color)
            )
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(pieChart).apply {
            duration = 600
        }
        pieChart.setPieData(pieData = pieData, animation = pieAnimation)
    }

    private fun setUpPrimaryCard(bills: List<Bill>) {
        val primaryList = viewModel.dailyPrimaryList(bills, "blue")
        dailyPrimaryDetailRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            if (cnt == 0) addItemDecoration(getItemDecoration())
            adapter = DailyPrimaryOverviewAdapter(primaryList)
        }
        setUpPrimaryPieView(primaryList)
    }

    private fun setUpPrimaryPieView(primaryList: List<DailyPrimary>) {
        daily_primary_title.text = "分类"
        val piePortions = primaryList.map {
            PiePortion(
                it.primaryCategory, it.amount.absoluteValue, ContextCompat.getColor(requireContext(), it.color)
            )
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(dailyPrimaryOverviewPie).apply {
            duration = 600
        }

        dailyPrimaryOverviewPie.setPieData(pieData = pieData, animation = pieAnimation)
    }

    private fun setUpAccountCard(bills: List<Bill>) {
        val accountList = viewModel.dailyAccountList(bills, "purple")
        dailyAccountDetailRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            if (cnt == 0) addItemDecoration(getItemDecoration())
            adapter = DailyAccountOverviewAdapter(accountList)
        }
        setUpAccountPieView(accountList)
    }

    private fun setUpAccountPieView(accountList: List<DailyAccount>) {
        dailyAccountTitle.text = "账户"
        val piePortions = accountList.map {
            PiePortion(
                it.account, it.amount.absoluteValue, ContextCompat.getColor(requireContext(), it.color)
            )
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(dailyAccountOverviewPie).apply {
            duration = 600
        }

        dailyAccountOverviewPie.setPieData(pieData = pieData, animation = pieAnimation)
    }

    private fun setUpMemberCard(bills: List<Bill>) {
        val memberList = viewModel.dailyMemberList(bills, "orange")
        dailyMemberDetailRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            if (cnt == 0) addItemDecoration(getItemDecoration())
            adapter = DailyMemberOverviewAdapter(memberList)
        }
        setUpMemberPieView(memberList)
    }

    private fun setUpMemberPieView(memberList: List<DailyMember>) {
        dailyMemberTitle.text = "成员"
        val piePortions = memberList.map {
            PiePortion(
                it.member, it.amount.absoluteValue, ContextCompat.getColor(requireContext(), it.color)
            )
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(dailyMemberOverviewPie).apply {
            duration = 600
        }

        dailyMemberOverviewPie.setPieData(pieData = pieData, animation = pieAnimation)
    }

    private fun setUpTypeCard(bills: List<Bill>) {
        val typeList = viewModel.dailyTypeList(bills)
        dailyTypeDetailRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            if (cnt == 0) addItemDecoration(getItemDecoration())
            adapter = DailyTypeOverviewAdapter(typeList)
        }
        setUpTypePieView(typeList)
    }

    private fun setUpTypePieView(typeList: List<DailyType>) {
        dailyTypeTitle.text = "收支"
        val piePortions = typeList.map {
            PiePortion(
                it.type, it.amount.absoluteValue, ContextCompat.getColor(requireContext(), it.color)
            )
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(dailyTypeOverviewPie).apply {
            duration = 600
        }

        dailyTypeOverviewPie.setPieData(pieData = pieData, animation = pieAnimation)
    }

    companion object
}