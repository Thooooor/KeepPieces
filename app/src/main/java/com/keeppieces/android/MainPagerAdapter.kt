package com.keeppieces.android

import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.keeppieces.android.ui.daily.DailyFragment
import com.keeppieces.android.ui.monthly.MonthlyFragment
import com.keeppieces.android.ui.overview.OverviewFragment
import java.time.LocalDate

class MainPagerAdapter(
    fm: FragmentManager,
    private val tabs: List<TabUiModel>
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MonthlyFragment()
            1 -> DailyFragment(LocalDate.now())
            else -> MonthlyFragment()
        }
    }

    override fun getCount(): Int {
        return tabs.size
    }
}

fun generateTabs(): List<TabUiModel> {
    return listOf(
        TabUiModel("Overview", R.drawable.ic_overview),
        TabUiModel("Accounts", R.drawable.ic_today),
        TabUiModel("Bills", R.drawable.ic_date_range),
        TabUiModel("Budget", R.drawable.ic_chart),
        TabUiModel("Setting", R.drawable.ic_settings)
    )
}

data class TabUiModel(val name: String, @DrawableRes val icon: Int)