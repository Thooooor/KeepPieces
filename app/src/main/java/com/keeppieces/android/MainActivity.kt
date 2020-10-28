package com.keeppieces.android

import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.keeppieces.android.extension.toCHINADFormatted
import com.keeppieces.android.ui.overview.AddMonthBudgetDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_month_summary_card.*

@Suppress("COMPATIBILITY_WARNING")
class MainActivity : AppCompatActivity(),AddMonthBudgetDialog.SetMonthBudgetInterface {
    val context = KeepPiecesApplication.context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) runEnterAnimation()
        setUpViewPager()
    }

    private fun setUpViewPager() {
        val tabs = generateTabs()
        view_pager.adapter = MainPagerAdapter(supportFragmentManager, tabs)
        view_pager.offscreenPageLimit = 0
        tab_layout.setUpWithViewPager(view_pager, true)
        view_pager.setCurrentItem(0, true)
        view_pager.adapter
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
        button_set_month_budget.text = monthBudget.toDouble().toCHINADFormatted()
    }
}