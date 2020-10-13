package com.keeppieces.android.ui.daily

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
import com.keeppieces.pie_chart.PieAnimation
import com.keeppieces.pie_chart.PieData
import com.keeppieces.pie_chart.PiePortion
import kotlinx.android.synthetic.main.fragment_daily.*
import kotlinx.android.synthetic.main.layout_daily_account_overview.*
import kotlinx.android.synthetic.main.layout_daily_member_overview.*
import kotlinx.android.synthetic.main.layout_daily_primary_overview.*
import kotlinx.android.synthetic.main.layout_daily_type_overview.*
import java.time.LocalDate


class DailyFragment : Fragment() {
    private val viewModel: DailyViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_daily, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpView()
        Log.d(TAG, "onActivityCreated")
    }

    private fun setUpView() {
        viewModel.billList(LocalDate.now().toString()).observe(viewLifecycleOwner) { billList ->
            val bills = if (billList.isEmpty()) tempList else billList
            setUpPieView(bills)
            setUpTypeCard(bills)
            setUpPrimaryCard(bills)
            setUpAccountCard(bills)
            setUpMemberCard(bills)
        }
    }

    private fun setUpPieView(bills: List<Bill>) {
        val dailyOverview = viewModel.dailyOverview(bills, "green")
        dailyAmount.text = dailyOverview.total.toCHINADFormatted()
        val piePortions = dailyOverview.bills.map {
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
        val primaryList = viewModel.dailyPrimaryList(bills, "blue")
        daily_primary_detail_recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
            adapter = DailyPrimaryOverviewAdapter(primaryList)
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
        val pieAnimation = PieAnimation(daily_primary_overview_pie).apply {
            duration = 600
        }

        daily_primary_overview_pie.setPieData(pieData = pieData, animation = pieAnimation)
    }

    private fun setUpAccountCard(bills: List<Bill>) {
        val accountList = viewModel.dailyAccountList(bills, "purple")
        daily_account_detail_recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
            adapter = DailyAccountOverviewAdapter(accountList)
        }
        setUpAccountPieView(accountList)
    }

    private fun setUpAccountPieView(accountList: List<DailyAccount>) {
        daily_account_title.text = "账户"
        val piePortions = accountList.map {
            PiePortion(
                it.account, it.amount, ContextCompat.getColor(requireContext(), it.color)
            )
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(daily_account_overview_pie).apply {
            duration = 600
        }

        daily_account_overview_pie.setPieData(pieData = pieData, animation = pieAnimation)
    }

    private fun setUpMemberCard(bills: List<Bill>) {
        val memberList = viewModel.dailyMemberList(bills, "orange")
        daily_member_detail_recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
            adapter = DailyMemberOverviewAdapter(memberList)
        }
        setUpMemberPieView(memberList)
    }

    private fun setUpMemberPieView(memberList: List<DailyMember>) {
        daily_member_title.text = "成员"
        val piePortions = memberList.map {
            PiePortion(
                it.member, it.amount, ContextCompat.getColor(requireContext(), it.color)
            )
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(daily_member_overview_pie).apply {
            duration = 600
        }

        daily_member_overview_pie.setPieData(pieData = pieData, animation = pieAnimation)
    }

    private fun setUpTypeCard(bills: List<Bill>) {
        val typeList = viewModel.dailyTypeList(bills, "yellow")
        daily_type_detail_recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
            adapter = DailyTypeOverviewAdapter(typeList)
        }
        setUpTypePieView(typeList)
    }

    private fun setUpTypePieView(typeList: List<DailyType>) {
        daily_type_title.text = "收支"
        val piePortions = typeList.map {
            PiePortion(
                it.type, it.amount, ContextCompat.getColor(requireContext(), it.color)
            )
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(daily_type_overview_pie).apply {
            duration = 600
        }

        daily_type_overview_pie.setPieData(pieData = pieData, animation = pieAnimation)
    }

    companion object {
        const val TAG = "DailyFragment"
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
        )
    }
}