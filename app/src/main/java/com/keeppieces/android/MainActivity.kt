package com.keeppieces.android

import android.os.Build
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.keeppieces.android.extension.toCHINADFormatted
import com.keeppieces.android.ui.overview.AddMonthBudgetDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_month_summary_card.*
import java.time.LocalDate
import java.time.chrono.IsoChronology
import java.util.*

var MainToday: String = ""
var MainStartDate: String = ""
var MainEndDate: String = ""

@Suppress("COMPATIBILITY_WARNING")
class MainActivity : AppCompatActivity(),AddMonthBudgetDialog.SetMonthBudgetInterface {
    val context = KeepPiecesApplication.context
    private val today = Calendar.getInstance()
    private val year = today.get(Calendar.YEAR)
    private var month = today.get(Calendar.MONTH) + 1 // Calendar.MONTH 范围为 0~11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDate()
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) runEnterAnimation()
        setUpViewPager()
    }

    fun initDate() {
        val lastDay = when(month) {
            2 -> if (IsoChronology.INSTANCE.isLeapYear(year.toLong())) 29 else 28
            4 -> 30
            6 -> 30
            9 -> 30
            11 -> 30
            else -> 31
        }
        MainToday = LocalDate.now().toString()
        MainStartDate = LocalDate.of(year, month, 1).toString()
        MainEndDate = LocalDate.of(year, month, lastDay).toString()
    }

    private fun setUpViewPager() {
        val tabs = generateTabs()
        view_pager.adapter = MainPagerAdapter(supportFragmentManager, tabs)
        view_pager.offscreenPageLimit = 0
        tab_layout.setUpWithViewPager(view_pager, true)
        view_pager.setCurrentItem(0, true)
    }

    private fun runEnterAnimation() {
        tab_layout.post {
            tab_layout.translationY -= tab_layout.height.toFloat()
            tab_layout.alpha = 0f
            tab_layout.animate()
                .translationY(0f)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .alpha(1f)
                .setDuration(300)
                .start()
        }
    }

    override fun setMonthBudgetButtonText(monthBudget: String) {
        set_month_budget.text = monthBudget.toDouble().toCHINADFormatted()
    }
}