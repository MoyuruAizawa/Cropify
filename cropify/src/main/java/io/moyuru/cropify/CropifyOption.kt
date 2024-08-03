package io.moyuru.cropify

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class CropifyOption(
  val frameColor: Color = Color.White,
  val frameAlpha: Float = 0.8f,
  val frameWidth: Dp = 2.dp,
  val gridColor: Color = Color.White,
  val gridAlpha: Float = 0.6f,
  val gridWidth: Dp = 1.dp,
  val maskColor: Color = Color.Black,
  val maskAlpha: Float = 0.5f,
  val backgroundColor: Color = Color.Black,
  val frameSize: CropifySize? = null,
)