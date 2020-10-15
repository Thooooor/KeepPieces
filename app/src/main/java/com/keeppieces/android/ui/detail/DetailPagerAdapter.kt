package com.keeppieces.android.ui.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class DetailPagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(
    fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
){
    override fun getItem(position: Int): Fragment {
        return DetailFragment.newInstance()
    }

    override fun getCount(): Int {
        return 12
    }
}