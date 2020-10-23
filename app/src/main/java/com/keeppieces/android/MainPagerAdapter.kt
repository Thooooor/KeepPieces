package com.keeppieces.android

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.keeppieces.android.ui.daily.DailyFragment
import com.keeppieces.android.ui.monthly.MonthlyFragment
import com.keeppieces.android.ui.overview.OverviewFragment
import com.keeppieces.android.ui.settings.SettingsFragment
import java.time.LocalDate

class MainPagerAdapter(
    fm: FragmentManager,
    private val tabs: List<TabUiModel>
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OverviewFragment()
            1 -> DailyFragment(LocalDate.now().toString())
            2 -> MonthlyFragment(LocalDate.now().toString(), LocalDate.now().toString())
            4 -> SettingsFragment()
            else -> DailyFragment(LocalDate.now().toString())
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