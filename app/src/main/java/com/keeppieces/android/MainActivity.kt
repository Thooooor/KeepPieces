package com.keeppieces.android

import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("COMPATIBILITY_WARNING")
class MainActivity : AppCompatActivity() {
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
        tab_layout.setUpWithViewPager(view_pager, false)
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

}