package com.keeppieces.scrollable_tab

import kotlin.math.pow


object Util {

  /**
   * This method is from medium article by supha software with a little params modification
   */
  fun getGaussianScale(
      childCenterX: Int,
      minScaleOffset: Float,
      scaleFactor: Float,
      spreadFactor: Double,
      left: Int,
      right: Int
  ): Float {
    val recyclerCenterX = (left + right) / 2
    return (Math.E.pow(
        -(childCenterX - recyclerCenterX.toDouble()).pow(2.toDouble()) / (2 * spreadFactor.pow(2.toDouble()))
    ) * scaleFactor + minScaleOffset).toFloat()
  }
}