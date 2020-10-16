package com.keeppieces.android.ui.monthly

import android.os.Build
import android.os.Bundle
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
import java.time.LocalDate
import java.util.*

class MonthlyFragment: Fragment() {
    private val viewModel: MonthlyViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_monthly, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val today = Calendar.getInstance()
        val year = today.get(Calendar.YEAR)
        val month = today.get(Calendar.MONTH)
        val day = today.get(Calendar.DAY_OF_MONTH)
        val startDate = Calendar.getInstance()
        startDate.set(year, month, 1)
        val endDate = Calendar.getInstance()
        endDate.set(year, month+1, 1)
        endDate.add(Calendar.DAY_OF_MONTH, -1)
        val date = LocalDate.now()

        setUpView(date.toString())
        monthlyLeftArrow.setOnClickListener {
            date.plusDays(-1)
            setUpView(date.toString())
        }
        monthlyRightArrow.setOnClickListener {
            date.plusDays(1)
            setUpView(date.toString())
        }
    }

    private fun setUpView(date: String) {
        viewModel.billList(date, date).observe(viewLifecycleOwner) { billList ->
            val bills = if (billList.isEmpty()) tempList else billList
            setUpPieView(bills)
            setUpTypeCard(bills)
            setUpPrimaryCard(bills)
            setUpAccountCard(bills)
            setUpMemberCard(bills)
        }

        monthlyDetailBtn.setOnClickListener {
            DetailActivity.start(it.context, LocalDate.now(), LocalDate.now(), R.color.dark_green)
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
            addItemDecoration(getItemDecoration())
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
            addItemDecoration(getItemDecoration())
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
            addItemDecoration(getItemDecoration())
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
        dailyTypeDetaiRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
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