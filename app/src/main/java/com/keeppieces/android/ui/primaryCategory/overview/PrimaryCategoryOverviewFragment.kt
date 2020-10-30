package com.keeppieces.android.ui.primaryCategory.overview

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.keeppieces.android.R
import com.keeppieces.pie_chart.PieAnimation
import com.keeppieces.pie_chart.PieData
import com.keeppieces.pie_chart.PiePortion
import kotlinx.android.synthetic.main.fragment_monthly.monthlyLeftArrow
import kotlinx.android.synthetic.main.fragment_monthly.monthlyRightArrow
import kotlinx.android.synthetic.main.fragment_monthly.pieChart
import kotlinx.android.synthetic.main.fragment_primary.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.chrono.IsoChronology
import kotlin.math.abs

const val DayMode = 0
const val MonthMode = 1
const val CustomMode = 2

class PrimaryCategoryOverviewFragment(var startDate: String, var endDate: String): Fragment() {
    private val viewModel by lazy { ViewModelProvider(requireActivity()).get(
        PrimaryCategoryOverViewViewModel::class.java) }
    private lateinit var startLocalDate: LocalDate
    private lateinit var endLocalDate: LocalDate
    private var timeSpan: Int = 1
    private var cnt: Int = -1
    private var mode = MonthMode

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_primary, container, false)
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initDate() // 得到最开始传入的时间长度（左闭右闭）timespan
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpView() {
        if (cnt <= 0) cnt++
        val billListInTimeSpan = viewModel.billList(startDate,endDate)
        billListInTimeSpan.observe(viewLifecycleOwner) { billList ->
            viewModel.getPrimaryClassification(billList) // 对 bill 按一级类分类，分类结果保存在viewModel中
            viewModel.getPrimarySummary("blue","yellow")
            timeSpanView.text = StringBuilder("$startDate ~ $endDate").toString()
            setUpPrimaryPieView()
            setUpPrimaryRecyclerView()
        }
    }
    private fun setUpPrimaryPieView() {
        val piePortions = viewModel.primarySummary.map {
            PiePortion(
                it.primaryCategory, abs(it.amount), ContextCompat.getColor(requireContext(), it.color)
            )// 这里的amount正负由颜色确定
        }.toList()
        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(pieChart).apply {
            duration = 600
        }
        pieChart.setPieData(pieData = pieData, animation = pieAnimation)
    }

    private fun setUpPrimaryRecyclerView() {
        primaryOverviewRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
//            setHasFixedSize(true)
//            if (cnt == 0) addItemDecoration(getItemDecoration())
            adapter = PrimaryCategoryOverviewAdapter(viewModel.primarySummary,
                viewModel.primaryTotalIncomeExpenditure.second,
                viewModel.primaryTotalIncomeExpenditure.third,
                startDate,endDate
            )
        }
    }
}