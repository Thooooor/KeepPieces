package com.keeppieces.android.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.button.MaterialButton
import com.keeppieces.android.R
import com.keeppieces.android.ui.bill.BillActivity
import com.keeppieces.android.ui.viewpager.SwipeControllableViewPager
import kotlinx.android.synthetic.main.layout_top_tab.view.*

class TopTab @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var viewPager: SwipeControllableViewPager? = null
    var previousClickedPosition = 0
    var lastClickedPosition = 0

    private val transition by lazy {
        androidx.transition.AutoTransition().apply {
            excludeTarget(textView, true)
        }.apply {
            interpolator = FastOutSlowInInterpolator()
            duration = 300
        }
    }

    private val titleAlphaAnimator by lazy {
        ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f).apply {
            startDelay = duration * 1 / 3
            duration = 300
        }
    }

    private val titleSlideAnimator by lazy {
        val tvWidth = resources.displayMetrics.widthPixels - (image1.width * 5)
        ObjectAnimator.ofFloat(
            textView, "translationX", tvWidth.toFloat(), 0f
        ).apply {
            interpolator = FastOutSlowInInterpolator()
            duration = 300
        }
    }

    init {
        View.inflate(context, R.layout.layout_top_tab, this)

        val slideInRight = Slide(Gravity.RIGHT)
        slideInRight.duration = 1000
        slideInRight.startDelay = 0

        image1.setOnClickListener {
            viewPager?.setCurrentItem(0, true)
        }

        image2.setOnClickListener {
            viewPager?.setCurrentItem(1, true)
        }

        image3.setOnClickListener {
            viewPager?.setCurrentItem(2, true)
        }

        image4.setOnClickListener {
            viewPager?.setCurrentItem(3, true)
        }

        image5.setOnClickListener {
            val intent = Intent(context, BillActivity::class.java)
            startActivity(context, intent, bundleOf())
            // viewPager?.setCurrentItem(4, true)
        }
    }

    private val tabNames = listOf("校园账本", "每日详情", "每月详情", "类别详情", "账本设置")

    fun clickedItem(position: Int) {
        val flow = findViewById<Flow>(R.id.flow)
        val refs = flow.referencedIds.toMutableList()

        // Currently a bug
        // Found in ConstraintLayout flow that does not have no reference ids after configuration changes
        // Tweak it by switching tab to zero position
        if (refs.size < position) {
            viewPager?.currentItem = 0
            return
        }

        TransitionManager.beginDelayedTransition(cl, transition)
        previousClickedPosition = lastClickedPosition
        lastClickedPosition = position

        if (lastClickedPosition != previousClickedPosition) {
            textView.alpha = 0f

            refs.remove(R.id.textView)
            refs.filterIndexed { index, viewId -> index != position }
                .forEach {
                    findViewById<MaterialButton>(it).iconTint =
                        ColorStateList.valueOf(
                            fetchColor(com.keeppieces.design_system.R.attr.colorPrimaryVariant)
                        )
                }

            findViewById<MaterialButton>(refs[position]).iconTint =
                ColorStateList.valueOf(fetchColor(com.keeppieces.design_system.R.attr.colorPrimary))
            findViewById<TextView>(R.id.textView).setTextColor(
                fetchColor(com.keeppieces.design_system.R.attr.colorPrimary)
            )

            //requestLayout()

            textView.text = tabNames[position]

            if (previousClickedPosition < lastClickedPosition) {
                val set = AnimatorSet()

                set.playTogether(titleAlphaAnimator, titleSlideAnimator)
                set.start()
            } else {
                titleAlphaAnimator.start()
            }


            refs.add(position + 1, R.id.textView)

            flow.referencedIds = refs.toIntArray()
        }
    }

    private fun fetchColor(colorAttr: Int): Int {
        val typedValue = TypedValue()

        val a = context.obtainStyledAttributes(typedValue.data, intArrayOf(colorAttr))
        val color = a.getColor(0, 0)

        a.recycle()

        return color
    }

    fun setUpWithViewPager(
        viewPager: SwipeControllableViewPager,
        allowSwipe: Boolean
    ) {
        this.viewPager = viewPager
        this.viewPager?.swipeEnabled = false

        this.viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                clickedItem(position)
            }
        })
    }
}