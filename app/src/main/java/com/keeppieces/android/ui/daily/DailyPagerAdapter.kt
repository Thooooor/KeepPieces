package com.keeppieces.android.ui.daily

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class DailyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
  override fun getItem(position: Int): Fragment {
    return DailyFragment.newInstance(position)
  }

  override fun getCount(): Int {
    return 12
  }
}