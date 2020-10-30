package com.keeppieces.android.ui.primaryCategory.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.keeppieces.android.logic.data.Bill

class CategoryDetailActivityAdapter(
    fm: FragmentManager,
    private val tabSize: Int,
    private val incomeList: List<Bill>, private val expenditureList: List<Bill>,
    val startDate:String, val endDate:String,
    private val level:Int
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return tabSize
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> CategoryBillFlowFragment(expenditureList, startDate, endDate, level)  // 支出
            1 -> CategoryBillFlowFragment(incomeList,startDate, endDate, level)  // 收入
            else -> CategoryBillFlowFragment(expenditureList,startDate, endDate, level)  // 支出
        }

    override fun getPageTitle(position: Int) =
        when(position) {
            0 -> "支出"
            1 -> "收入"
            else -> "支出"
        }
}