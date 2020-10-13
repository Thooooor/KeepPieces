package com.keeppieces.pie_chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

class PieChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    val defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr) {
    var pieProgressRenderData = listOf<PieRenderData>()
    private var pieFinalRenderData = listOf<PieRenderData>()

    private val stroke = context.dpToPx(6f)
    private var rect = RectF()
    private var chartRadius = 500f
    private var centerX = 0.0f
    private var centerY = 0.0f

    private val paint by lazy {
        Paint().apply {
            strokeWidth = stroke
            isAntiAlias = true
            style = Paint.Style.STROKE

        }
    }

    private var colorPrimary : Int = 0

    init{
        // Get attrs
        colorPrimary = Color.parseColor("#2A2931")

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)

        chartRadius = (width / 2).toFloat() - paddingStart - paddingEnd - stroke - stroke

        rect.set(
            0f + stroke + paddingLeft,
            0f + stroke + paddingTop,
            width - stroke - paddingRight,
            height - stroke - paddingRight
        )
        centerX = rect.centerX()
        centerY = rect.centerY()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.color = colorPrimary
        canvas?.drawCircle(centerX , centerY , chartRadius + stroke , paint)

        canvas?.apply {
            pieFinalRenderData.forEachIndexed { index, it ->
                paint.color = it.color
                drawArc(
                    rect,
                    pieProgressRenderData[index].startAngle.toFloat(),
                    pieProgressRenderData[index].sweepAngle.toFloat(),
                    false,
                    paint
                )

            }
        }

    }


    override fun startAnimation(animation: Animation?) {
        if (animation is PieAnimation) {
            animation.addData(pieFinalRenderData)
        }
        super.startAnimation(animation)
    }

    fun setPieData(pieData: PieData, animation: PieAnimation? = null) {
        val totalPortionValues = pieData.maxValue ?: pieData.portions.sumByDouble { it.value }.toFloat()
        pieFinalRenderData = pieData.portions.toPoints(totalPortionValues.toFloat())
        if (animation != null) {
            animation.addData(pieFinalRenderData)
            animation.interpolator = FastOutSlowInInterpolator()
            this.startAnimation(animation)
        } else {
            pieProgressRenderData = pieFinalRenderData
        }
    }
}