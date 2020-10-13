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
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.logic.data.DailyPrimary
import com.keeppieces.android.ui.daily.adapter.DailyPrimaryOverviewAdapter
import com.keeppieces.pie_chart.PieAnimation
import com.keeppieces.pie_chart.PieData
import com.keeppieces.pie_chart.PiePortion
import kotlinx.android.synthetic.main.fragment_daily.*
import kotlinx.android.synthetic.main.layout_daily_primary_overview.*
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
            Log.d("Daily", "PieView done.")
            setUpPrimaryCard(bills)
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
        primary_detail_recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
            adapter = DailyPrimaryOverviewAdapter(primaryList)
        }
        Log.d("Daily", "PrimaryCard recycler done.")
        setUpPrimaryPieView(primaryList)
        Log.d("Daily", "PrimaryCard PieView done.")
    }

    private fun setUpPrimaryPieView(primaryList: List<DailyPrimary>) {
        primary_title.text = "分类"
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

    /*
    private fun setUpRecyclerView() {
        dailyBills.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
            viewModel.billList(LocalDate.now().toString()).observe(viewLifecycleOwner) { billList ->
                for (bill in billList) {
                    Log.d("Daily ${bill.billId}", bill.toString())
                }
                adapter = if (billList.isEmpty()) {
                    Log.d("DailyFragment", "ListEmpty")
                    DailyAdapter(tempList)
                } else {
                    DailyAdapter(billList)
                }
            }
        }
    }*/

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