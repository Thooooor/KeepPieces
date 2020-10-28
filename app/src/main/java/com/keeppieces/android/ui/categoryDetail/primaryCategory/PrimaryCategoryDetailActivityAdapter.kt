package com.keeppieces.android.ui.categoryDetail.primaryCategory

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.keeppieces.android.logic.data.Bill

class PrimaryCategoryDetailActivityAdapter(
    private val fm: FragmentManager,
    private val tabSize: Int,
    private val incomeList: List<Bill>, private val expenditureList: List<Bill>,
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return tabSize
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> SecondaryCategoryBillFlowFragment(expenditureList)  // 支出
            1 -> SecondaryCategoryBillFlowFragment(incomeList)  // 收入
            else -> SecondaryCategoryBillFlowFragment(expenditureList)  // 支出
        }

    override fun getPageTitle(position: Int) =
        when(position) {
            0 -> "支出"
            1 -> "收入"
            else -> "支出"
        }
}