package com.keeppieces.android.ui.account.adapter

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.keeppieces.android.R
import com.keeppieces.android.ui.account.AccountDetailFragment
import com.keeppieces.android.ui.daily.DailyFragment
import java.time.LocalDate
import java.util.*

class AccountPagerAdapter(
    fm: FragmentManager,
//    private val tabs: List<TabUiModel>,
    private val startDate : String,
    private val endDate: String,
    private val account : String
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val today = Calendar.getInstance()
    private val year = today.get(Calendar.YEAR)
    private var month = today.get(Calendar.MONTH) + 1 // Calendar.MONTH 范围为 0~11

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItem(position: Int): Fragment {
//        val lastDay = when(month) {
//            2 -> if (IsoChronology.INSTANCE.isLeapYear(year.toLong())) 29 else 28
//            4 -> 30
//            6 -> 30
//            9 -> 30
//            11 -> 30
//            else -> 31
//        }

//        val startDate = LocalDate.of(year, month, 1).toString()
//        val endDate = LocalDate.of(year, month, lastDay).toString()

        return when (position) {
            0 -> AccountDetailFragment(startDate, endDate, account, "支出")
            1 -> AccountDetailFragment(startDate, endDate, account, "收入")
            else -> DailyFragment(LocalDate.now().toString())
        }
    }

    override fun getCount(): Int {
        return 2
    }
}

fun generateTabs(): List<TabUiModel> {
    return listOf(
        TabUiModel("Income", R.drawable.ic_overview),
        TabUiModel("Accounts", R.drawable.ic_today),
        TabUiModel("Bills", R.drawable.ic_date_range),
        TabUiModel("Budget", R.drawable.ic_chart),
        TabUiModel("Setting", R.drawable.ic_settings)
    )
}

data class TabUiModel(val name: String, @DrawableRes val icon: Int)