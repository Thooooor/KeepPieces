package com.keeppieces.line_indicator.data


data class LineIndicatorData(
  val portions: List<LineIndicatorPortion>,
  val maxValue: Float? = null
)

data class LineIndicatorPortion(
  val name: String,
  val value: Float,
  val colorInt: Int
)

fun List<LineIndicatorPortion>.toPoints(maxValue: Float): List<LineIndicatorRenderData> {
  val renderDataList = mutableListOf<LineIndicatorRenderData>()
  forEachIndexed { _, it ->
    val percent = it.value / maxValue
    renderDataList.add(LineIndicatorRenderData(it.name, percent, it.colorInt))
  }

  return renderDataList
}

data class LineIndicatorRenderData(
  val name: String,
  val percentage: Float,
  val color: Int
)