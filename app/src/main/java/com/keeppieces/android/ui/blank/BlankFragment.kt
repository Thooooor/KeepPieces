package com.keeppieces.android.ui.blank

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.keeppieces.android.MainActivity
import com.keeppieces.android.MainEndDate
import com.keeppieces.android.MainStartDate
import com.keeppieces.android.R
import com.keeppieces.android.ui.primaryCategory.overview.PrimaryCategoryOverviewFragment
import com.keeppieces.android.ui.account.AccountFragment
import com.keeppieces.android.ui.member.MemberFragment
import com.keeppieces.android.ui.overview.getParentActivity
import kotlinx.android.synthetic.main.fragment_blank.*


const val AccountPage = 1
const val MemberPage = 2
const val CategoryPage = 3
var currentType: Int = CategoryPage

class BlankFragment() : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setFragment(currentType, MainStartDate, MainEndDate)
        changeFab.setOnClickListener {
            when (currentType) {
                AccountPage -> setFragment(CategoryPage, MainStartDate, MainEndDate)
                CategoryPage -> setFragment(MemberPage, MainStartDate, MainEndDate)
                MemberPage -> setFragment(AccountPage, MainStartDate, MainEndDate)
                else -> setFragment(AccountPage, MainStartDate, MainEndDate)
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setFragment(typePage:Int, startDate: String, endDate: String) {
        val fragmentManager = getParentActivity<MainActivity>().supportFragmentManager
        val transition = fragmentManager.beginTransaction()
        val newFragment = when (typePage) {
            AccountPage -> AccountFragment(startDate, endDate)
            MemberPage -> MemberFragment(startDate, endDate)
            CategoryPage -> PrimaryCategoryOverviewFragment(startDate, endDate)
            else -> PrimaryCategoryOverviewFragment(startDate, endDate)
        }
        when (typePage) {
            AccountPage -> changeFab.setImageDrawable(resources.getDrawable(R.drawable.ic_bill_account, resources.newTheme()))
            MemberPage -> changeFab.setImageDrawable(resources.getDrawable(R.drawable.ic_bill_member, resources.newTheme()))
            CategoryPage -> changeFab.setImageDrawable(resources.getDrawable(R.drawable.ic_bill_category, resources.newTheme()))
        }
        transition.replace(R.id.childBlankFragment, newFragment)
        transition.commit()
        currentType = typePage
    }
}