package com.keeppieces.android.ui.login

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Path
import android.util.Log
import android.view.View

/**
 * Created by minlewan on 2018/1/19.
 */

class GestureLockView(context: Context,
                      /**
                       * 四个颜色，可由用户自定义，初始化时由GestureLockViewGroup传入
                       */
                      private val mColorNoFingerInner: Int,
                      private val mColorNoFingerOuter: Int,
                      private val mColorFingerOn: Int,
                      private val mColorFingerUp: Int) : View(context, null) {

    /**
     * GestureLockView的当前状态
     */
    private var mCurrentStatus = Mode.STATUS_NO_FINGER

    /**
     * 宽度
     */
    private var mWidth: Int = 0
    /**
     * 高度
     */
    private var mHeight: Int = 0
    /**
     * 外圆半径
     */
    private var mRadius: Int = 0
    /**
     * 画笔的宽度
     */
    private val mStrokeWidth = 2

    /**
     * 圆心坐标
     */
    private var mCenterX: Int = 0
    private var mCenterY: Int = 0
    private val mPaint: Paint

    /**
     * 箭头（小三角最长边的一半长度 = mArrawRate * mWidth / 2 ）
     */
    private val mArrowRate = 0.333f
    var arrowDegree = -1
    private val mArrowPath: Path
    /**
     * 内圆的半径 = mInnerCircleRadiusRate * mRadus
     */
    private val mInnerCircleRadiusRate = 0.3f

    /**
     * GestureLockView的三种状态
     */
    enum class Mode {
        STATUS_NO_FINGER, STATUS_FINGER_ON, STATUS_FINGER_UP
    }

    init {
        Log.i("GestureLockView:", "构造方法被调用")
        this.setWillNotDraw(false)
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mArrowPath = Path()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.i("GestureLockView:", "onMeasure 方法被调用")
        mWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        mHeight = View.MeasureSpec.getSize(heightMeasureSpec)

        // 取长和宽中的小值
        mWidth = if (mWidth < mHeight) mWidth else mHeight
        mCenterY = mWidth / 2
        mCenterX = mCenterY
        mRadius = mCenterX
        mRadius -= mStrokeWidth / 2

        // 绘制三角形，初始时是个默认箭头朝上的一个等腰三角形，用户绘制结束后，根据由两个GestureLockView决定需要旋转多少度
        val mArrowLength = mWidth / 2 * mArrowRate
        mArrowPath.moveTo((mWidth / 2).toFloat(), (mStrokeWidth + 2).toFloat())
        mArrowPath.lineTo(mWidth / 2 - mArrowLength, mStrokeWidth.toFloat() + 2f
                + mArrowLength)
        mArrowPath.lineTo(mWidth / 2 + mArrowLength, mStrokeWidth.toFloat() + 2f
                + mArrowLength)
        mArrowPath.close()
        mArrowPath.fillType = Path.FillType.WINDING

    }

    override fun onDraw(canvas: Canvas) {
        Log.i("GestureLockView:", "onDraw 方法被调用")
        when (mCurrentStatus) {
            GestureLockView.Mode.STATUS_FINGER_ON -> {

                // 绘制外圆
                mPaint.style = Style.STROKE
                mPaint.color = mColorFingerOn
                mPaint.strokeWidth = 2f
                canvas.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius.toFloat(), mPaint)
                // 绘制内圆
                mPaint.style = Style.FILL
                canvas.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius * mInnerCircleRadiusRate, mPaint)
            }
            GestureLockView.Mode.STATUS_FINGER_UP -> {
                // 绘制外圆
                mPaint.color = mColorFingerUp
                mPaint.style = Style.STROKE
                mPaint.strokeWidth = 2f
                canvas.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius.toFloat(), mPaint)
                // 绘制内圆
                mPaint.style = Style.FILL
                canvas.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius * mInnerCircleRadiusRate, mPaint)
            }

            GestureLockView.Mode.STATUS_NO_FINGER -> {

                // 绘制外圆
                mPaint.style = Style.FILL
                mPaint.color = mColorNoFingerOuter
                canvas.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius.toFloat(), mPaint)
                // 绘制内圆
                mPaint.color = mColorNoFingerInner
                canvas.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius * mInnerCircleRadiusRate, mPaint)
            }
        }

    }


    /**
     * 设置当前模式并重绘界面
     *
     * @param mode
     */
    fun setMode(mode: Mode) {
        this.mCurrentStatus = mode
        invalidate()
    }

    companion object {
        private val TAG = this::class.qualifiedName ?: "GestureLockView"
    }
}