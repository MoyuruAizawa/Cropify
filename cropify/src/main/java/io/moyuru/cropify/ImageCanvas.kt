package io.moyuru.cropify

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap

@Composable
internal fun ImageCanvas(
  bitmap: ImageBitmap,
  offset: Offset,
  size: Size,
  option: CropifyOption,
  modifier: Modifier = Modifier,
) {
  Canvas(modifier = modifier) {
    drawRect(option.backgroundColor)
    drawImage(
      image = bitmap,
      dstSize = size.toInt(),
      dstOffset = offset.toInt(),
    )
  }
}
