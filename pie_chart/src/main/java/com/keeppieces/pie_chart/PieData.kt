package com.keeppieces.pie_chart

data class PieData(val portions: List<PiePortion>, val maxValue: Float? = null)

data class PiePortion(val name: String, val value: Float, val colorInt: Int)

fun List<PiePortion>.toPoints(maxValue: Float): List<PieRenderData> {
    val renderDataList = mutableListOf<PieRenderData>()
    forEachIndexed { index, it ->
        val startAngle = if (renderDataList.isEmpty()) {
            -90f + 1f
        } else {
            val last = renderDataList.last()
            last.startAngle + last.sweepAngle + 1f
        }

        val sweepAngle = it.value * 360 / maxValue - 1f

        renderDataList.add(PieRenderData(it.name, startAngle, sweepAngle, it.colorInt))
    }

    return renderDataList
}


data class PieRenderData(val name: String, val startAngle: Float, val sweepAngle: Float, val color: Int)
