package com.keeppieces.android.ui.login

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import com.keeppieces.android.R


/**
 * Created by minlewan on 2018/1/19.
 */

class GestureLockViewGroup @JvmOverloads constructor(context: Context, attrs: AttributeSet,
                                                     defStyle: Int = 0) : RelativeLayout(context, attrs, defStyle) {
    /**
     * 保存所有的GestureLockView
     */
    private var mGestureLockViews: Array<GestureLockView?>? = null
    /**
     * 每个边上的GestureLockView的个数
     */
    private var mCount = 4
    /**
     * 存储答案
     */
    private var mAnswer = intArrayOf(0, 1, 2, 5, 8)
    /**
     * 保存用户选中的GestureLockView的id
     */
    private val mChoose = ArrayList<Int>()

    private val mPaint: Paint
    /**
     * 每个GestureLockView中间的间距 设置为：mGestureLockViewWidth * 25%
     */
    private var mMarginBetweenLockView = 30
    /**
     * GestureLockView的边长 4 * mWidth / ( 5 * mCount + 1 )
     */
    private var mGestureLockViewWidth: Int = 0

    /**
     * GestureLockView无手指触摸的状态下内圆的颜色
     */
    private var mNoFingerInnerCircleColor = -0x6c6f70
    /**
     * GestureLockView无手指触摸的状态下外圆的颜色
     */
    private var mNoFingerOuterCircleColor = -0x1f2425
    /**
     * GestureLockView手指触摸的状态下内圆和外圆的颜色
     */
    private var mFingerOnColor = -0xc87037
    /**
     * GestureLockView手指抬起的状态下内圆和外圆的颜色
     */
    private var mFingerUpColor = -0x10000

    /**
     * 宽度
     */
    private var mWidth: Int = 0
    /**
     * 高度
     */
    private var mHeight: Int = 0

    private val mPath: Path?
    /**
     * 指引线的开始位置x
     */
    private var mLastPathX: Int = 0
    /**
     * 指引线的开始位置y
     */
    private var mLastPathY: Int = 0
    /**
     * 指引下的结束位置
     */
    private val mTmpTarget = Point()

    /**
     * 最大尝试次数
     */
    private var mTryTimes = 4
    /**
     * 回调接口
     */
    private var mOnGestureLockViewListener: OnGestureLockViewListener? = null

    init {
        /**
         * 获得所有自定义的参数的值
         */
        val a = context.theme.obtainStyledAttributes(attrs,
            R.styleable.GestureLockViewGroup, defStyle, 0)
        val n = a.indexCount

        for (i in 0 until n) {
            val attr = a.getIndex(i)
            when (attr) {
                R.styleable.GestureLockViewGroup_color_no_finger_inner_circle -> mNoFingerInnerCircleColor = a.getColor(attr,
                    mNoFingerInnerCircleColor)
                R.styleable.GestureLockViewGroup_color_no_finger_outer_circle -> mNoFingerOuterCircleColor = a.getColor(attr,
                    mNoFingerOuterCircleColor)
                R.styleable.GestureLockViewGroup_color_finger_on -> mFingerOnColor = a.getColor(attr, mFingerOnColor)
                R.styleable.GestureLockViewGroup_color_finger_up -> mFingerUpColor = a.getColor(attr, mFingerUpColor)
                R.styleable.GestureLockViewGroup_count -> mCount = a.getInt(attr, 3)
                R.styleable.GestureLockViewGroup_tryTimes -> mTryTimes = a.getInt(attr, 5)
                else -> {
                }
            }
        }

        a.recycle()

        // 初始化画笔
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.style = Paint.Style.STROKE
        // mPaint.setStrokeWidth(20);
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeJoin = Paint.Join.ROUND
        // mPaint.setColor(Color.parseColor("#aaffffff"));
        mPath = Path()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        mHeight = View.MeasureSpec.getSize(heightMeasureSpec)

        // Log.e(TAG, mWidth + "");
        // Log.e(TAG, mHeight + "");

        mWidth = if (mWidth < mHeight) mWidth else mHeight
        mHeight = mWidth

        // setMeasuredDimension(mWidth, mHeight);

        // 初始化mGestureLockViews
        if (mGestureLockViews == null) {
            mGestureLockViews = arrayOfNulls<GestureLockView>(mCount * mCount)
            // 计算每个GestureLockView的宽度
            mGestureLockViewWidth = (4f * mWidth.toFloat() * 1.0f / (5 * mCount + 1)).toInt()
            //计算每个GestureLockView的间距
            mMarginBetweenLockView = (mGestureLockViewWidth * 0.25).toInt()
            // 设置画笔的宽度为GestureLockView的内圆直径稍微小点（不喜欢的话，随便设）
            mPaint.strokeWidth = mGestureLockViewWidth * 0.29f

            for (i in mGestureLockViews!!.indices) {
                //初始化每个GestureLockView
                mGestureLockViews!![i] = GestureLockView(context,
                    mNoFingerInnerCircleColor, mNoFingerOuterCircleColor,
                    mFingerOnColor, mFingerUpColor)
                mGestureLockViews!![i]!!.id = i + 1
                //设置参数，主要是定位GestureLockView间的位置
                val lockerParams = RelativeLayout.LayoutParams(
                    mGestureLockViewWidth, mGestureLockViewWidth)

                // 不是每行的第一个，则设置位置为前一个的右边
                if (i % mCount != 0) {
                    lockerParams.addRule(RelativeLayout.RIGHT_OF,
                        mGestureLockViews!![i - 1]!!.id)
                }
                // 从第二行开始，设置为上一行同一位置View的下面
                if (i > mCount - 1) {
                    lockerParams.addRule(RelativeLayout.BELOW,
                        mGestureLockViews!![i - mCount]!!.id)
                }
                //设置右下左上的边距
                val rightMargin = mMarginBetweenLockView
                val bottomMargin = mMarginBetweenLockView
                var leftMagin = 0
                var topMargin = 0
                /**
                 * 每个View都有右外边距和底外边距 第一行的有上外边距 第一列的有左外边距
                 */
                if (i >= 0 && i < mCount)
                // 第一行
                {
                    topMargin = mMarginBetweenLockView
                }
                if (i % mCount == 0)
                // 第一列
                {
                    leftMagin = mMarginBetweenLockView
                }

                lockerParams.setMargins(leftMagin, topMargin, rightMargin,
                    bottomMargin)
                mGestureLockViews!![i]!!.setMode(GestureLockView.Mode.STATUS_NO_FINGER)
                addView(mGestureLockViews!![i], lockerParams)
            }

            Log.e(TAG, "mWidth = " + mWidth + " ,  mGestureViewWidth = "
                    + mGestureLockViewWidth + " , mMarginBetweenLockView = "
                    + mMarginBetweenLockView)

        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val x = event.x.toInt()
        val y = event.y.toInt()

        when (action) {
            MotionEvent.ACTION_DOWN ->
                // 重置
                reset()
            MotionEvent.ACTION_MOVE -> {
                mPaint.color = mFingerOnColor
                mPaint.alpha = 50
                val child = getChildIdByPos(x, y)
                if (child != null) {
                    val cId = child.id
                    if (!mChoose.contains(cId)) {
                        mChoose.add(cId)
                        child.setMode(GestureLockView.Mode.STATUS_FINGER_ON)
                        if (mOnGestureLockViewListener != null)
                            mOnGestureLockViewListener!!.onBlockSelected(cId)
                        // 设置指引线的起点
                        mLastPathX = child.left / 2 + child.right / 2
                        mLastPathY = child.top / 2 + child.bottom / 2

                        if (mChoose.size == 1)
                        // 当前添加为第一个
                        {
                            mPath!!.moveTo(mLastPathX.toFloat(), mLastPathY.toFloat())
                        } else
                        // 非第一个，将两者使用线连上
                        {
                            mPath!!.lineTo(mLastPathX.toFloat(), mLastPathY.toFloat())
                        }

                    }
                }
                // 指引线的终点
                mTmpTarget.x = x
                mTmpTarget.y = y
            }
            MotionEvent.ACTION_UP -> {

                mPaint.color = mFingerUpColor
                mPaint.alpha = 50
                this.mTryTimes--

                // 回调是否成功
                if (mOnGestureLockViewListener != null && mChoose.size > 0) {
                    mOnGestureLockViewListener!!.onGestureEvent(getmChoose())// 手离开后，调用接口 判断是否正确
                    if (this.mTryTimes == 0)
                    // 如果用完了最后一次
                    {
                        mOnGestureLockViewListener!!.onUnmatchedExceedBoundary()// 手离开后，调用接口 做出相应操作
                    }
                }

                Log.e(TAG, "mUnMatchExceedBoundary = $mTryTimes")
                Log.e(TAG, "mChoose = $mChoose")
                // 将终点设置位置为起点，即取消指引线
                mTmpTarget.x = mLastPathX
                mTmpTarget.y = mLastPathY
            }
        }// 改变子元素的状态为UP
        //                changeItemMode();
        // 计算每个元素中箭头需要旋转的角度
        //                for (int i = 0; i + 1 < mChoose.size(); i++)
        //                {
        //                    int childId = mChoose.get(i);
        //                    int nextChildId = mChoose.get(i + 1);
        //
        //                    GestureLockView startChild = (GestureLockView) findViewById(childId);
        //                    GestureLockView nextChild = (GestureLockView) findViewById(nextChildId);
        //
        //                    int dx = nextChild.getLeft() - startChild.getLeft();
        //                    int dy = nextChild.getTop() - startChild.getTop();
        //                    // 计算角度
        //                    int angle = (int) Math.toDegrees(Math.atan2(dy, dx)) + 90;
        //                    startChild.setArrowDegree(angle);
        //                }
        invalidate()
        return true
    }

    private fun changeItemMode() {
        for (gestureLockView in mGestureLockViews!!) {
            if (mChoose.contains(gestureLockView!!.id)) {
                gestureLockView.setMode(GestureLockView.Mode.STATUS_FINGER_UP)
            }
        }
    }

    /**
     *
     * 做一些必要的重置
     */
    fun reset() {
        mChoose.clear()
        mPath!!.reset()
        for (gestureLockView in mGestureLockViews!!) {
            gestureLockView!!.setMode(GestureLockView.Mode.STATUS_NO_FINGER)
            gestureLockView.arrowDegree = -1
        }
    }

    /**
     * 检查用户绘制的手势是否正确
     * @return
     */
    fun checkAnswer(): Boolean {
        if (mAnswer.size != mChoose.size)
            return false

        for (i in mAnswer.indices) {
            if (mAnswer[i] != mChoose[i])
                return false
        }

        return true
    }

    /**
     * 检查当前左边是否在child中
     * @param child
     * @param x
     * @param y
     * @return
     */
    private fun checkPositionInChild(child: View, x: Int, y: Int): Boolean {

        //设置了内边距，即x,y必须落入下GestureLockView的内部中间的小区域中，可以通过调整padding使得x,y落入范围不变大，或者不设置padding
        val padding = (mGestureLockViewWidth * 0.15).toInt()

        return (x >= child.left + padding && x <= child.right - padding
                && y >= child.top + padding
                && y <= child.bottom - padding)
    }

    /**
     * 通过x,y获得落入的GestureLockView
     * @param x
     * @param y
     * @return
     */
    private fun getChildIdByPos(x: Int, y: Int): GestureLockView? {
        for (gestureLockView in mGestureLockViews!!) {
            if (checkPositionInChild(gestureLockView!!, x, y)) {
                return gestureLockView
            }
        }

        return null

    }

    /**
     * 获得mChoose也就是选择的密码集合
     * @return
     */
    private fun getmChoose(): List<Int> {
        return mChoose
    }

    /**
     * 设置回调接口
     *
     * @param listener
     */
    fun setOnGestureLockViewListener(listener: OnGestureLockViewListener) {
        this.mOnGestureLockViewListener = listener
    }

    /**
     * 对外公布设置答案的方法
     *
     * @param answer
     */
    fun setAnswer(answer: IntArray) {
        this.mAnswer = answer
    }

    /**
     * 设置最大实验次数
     *
     * @param boundary
     */
    fun setUnMatchExceedBoundary(boundary: Int) {
        this.mTryTimes = boundary
    }

    public override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        //绘制GestureLockView间的连线
        if (mPath != null) {
            canvas.drawPath(mPath, mPaint)
        }
        //绘制指引线
        if (mChoose.size > 0) {
            if (mLastPathX != 0 && mLastPathY != 0)
                canvas.drawLine(mLastPathX.toFloat(), mLastPathY.toFloat(), mTmpTarget.x.toFloat(),
                    mTmpTarget.y.toFloat(), mPaint)
        }

    }

    interface OnGestureLockViewListener {
        /**
         * 单独选中元素的Id
         *
         * @param cId
         */
        fun onBlockSelected(cId: Int)

        /**
         * 是否匹配
         *
         * @param mChoose
         */
        fun onGestureEvent(mChoose: List<Int>){

        }

        /**
         * 超过尝试次数
         */
        fun onUnmatchedExceedBoundary()
    }

    companion object {

        private val TAG = "GestureLockViewGroup"
    }
}