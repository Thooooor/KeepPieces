package com.keeppieces.android.ui.account.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.ui.account.AccountDetailFragment
import com.keeppieces.android.ui.daily.DailyFragment
import java.util.*

class AccountPagerAdapter(
        fm: FragmentManager,
        private val tabSize: Int,
        private val billsInFilter: MutableList<Bill>,
        private val billsOutFilter: MutableList<Bill>,
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val today = Calendar.getInstance()
    private val year = today.get(Calendar.YEAR)
    private var month = today.get(Calendar.MONTH) + 1 // Calendar.MONTH 范围为 0~11

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> AccountDetailFragment(billsOutFilter, "支出")
            1 -> AccountDetailFragment(billsInFilter, "收入")
            else -> DailyFragment()
        }
    }

    override fun getCount(): Int {
        return tabSize
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getPageTitle(position: Int) =
            when(position) {
                0 -> "支出"
                1 -> "收入"
                else -> "支出"
            }
}