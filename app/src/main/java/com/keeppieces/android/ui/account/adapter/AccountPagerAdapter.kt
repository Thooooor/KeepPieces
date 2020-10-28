package com.keeppieces.android.ui.account.adapter

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
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

data class TabUiModel(val name: String, @DrawableRes val icon: Int)