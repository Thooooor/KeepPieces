package com.ycuwq.datepicker.util

import android.graphics.Color

/**
 * 颜色线性渐变工具
 * Created by ycuwq on 2018/1/6.
 */
class BillLinearGradient(private var mStartColor: Int, private var mEndColor: Int) {
    private var mRedStart = 0
    private var mBlueStart = 0
    private var mGreenStart = 0
    private var mRedEnd = 0
    private var mBlueEnd = 0
    private var mGreenEnd = 0
    fun setStartColor(startColor: Int) {
        mStartColor = startColor
        updateColor()
    }

    fun setEndColor(endColor: Int) {
        mEndColor = endColor
        updateColor()
    }

    private fun updateColor() {
        mRedStart = Color.red(mStartColor)
        mBlueStart = Color.blue(mStartColor)
        mGreenStart = Color.green(mStartColor)
        mRedEnd = Color.red(mEndColor)
        mBlueEnd = Color.blue(mEndColor)
        mGreenEnd = Color.green(mEndColor)
    }

    fun getColor(ratio: Float): Int {
        val red = (mRedStart + ((mRedEnd - mRedStart) * ratio + 0.5)).toInt()
        val greed = (mGreenStart + ((mGreenEnd - mGreenStart) * ratio + 0.5)).toInt()
        val blue = (mBlueStart + ((mBlueEnd - mBlueStart) * ratio + 0.5)).toInt()
        return Color.rgb(red, greed, blue)
    }

    init {
        updateColor()
    }
}