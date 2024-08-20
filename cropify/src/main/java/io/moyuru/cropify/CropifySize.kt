package io.moyuru.cropify

import androidx.annotation.FloatRange

sealed interface CropifySize {

  data class PercentageSize(
    @FloatRange(0.0, 1.0, fromInclusive = false)
    val widthPercentage: Float,
    @FloatRange(0.0, 1.0, fromInclusive = false)
    val heightPercentage: Float
  ) : CropifySize {

    constructor(
      @FloatRange(0.0, 1.0, fromInclusive = false)
      percentage: Float
    ) : this(percentage, percentage)

    init {
      require(widthPercentage > 0f && widthPercentage <= 1f) { "widthPercentage must be more than 0f and less or equal to 1f" }
      require(heightPercentage > 0f && heightPercentage <= 1f) { "heightPercentage must be more than 0f and less or equal to 1f" }
    }

    companion object {
      val FullSize = PercentageSize(1f)
    }

  }

  data class FixedAspectRatio(val value: Float) : CropifySize {
    constructor(x: Int, y: Int) : this(y / x.toFloat())
    constructor(x: Float, y: Float) : this(y / x)
  }
}