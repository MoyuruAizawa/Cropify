package io.moyuru.cropify

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp

@Composable
internal fun ImageOverlay(
  offset: Offset,
  size: Size,
  tolerance: Float,
  option: CropifyOption,
  modifier: Modifier = Modifier,
) {
  Canvas(modifier = modifier) {
    with(drawContext.canvas.nativeCanvas) {
      val checkPoint = saveLayer(null, null)
      drawRect(
        color = option.maskColor,
        alpha = option.maskAlpha,
      )
      drawRect(
        color = Color.Transparent,
        topLeft = offset,
        size = size,
        blendMode = BlendMode.SrcOut
      )
      restoreToCount(checkPoint)
    }
    drawFrame(offset, size, option)
    drawCorner(offset, size, tolerance / 2, option)
    drawGrid(offset, size, option)
  }
}

private fun DrawScope.drawFrame(
  offset: Offset,
  size: Size,
  option: CropifyOption,
) {
  drawRect(
    color = option.frameColor,
    alpha = option.frameAlpha,
    topLeft = offset,
    size = size,
    style = Stroke(option.frameWidth.toPx())
  )
}

private fun DrawScope.drawCorner(
  offset: Offset,
  size: Size,
  cornerLength: Float,
  option: CropifyOption,
) {
  val rect = Rect(offset, size)
  val width = option.frameWidth.toPx()
  val offsetFromVertex = 4.dp.toPx()

  rect.topLeft
    .translate(offsetFromVertex, offsetFromVertex)
    .let { start ->
      drawLine(
        start = start.translateX(-width / 2),
        end = start.translateX(cornerLength),
        color = option.frameColor,
        alpha = option.frameAlpha,
        strokeWidth = width
      )
      drawLine(
        start = start,
        end = start.translateY(cornerLength),
        color = option.frameColor,
        alpha = option.frameAlpha,
        strokeWidth = width
      )
    }
  rect.topRight
    .translate(-offsetFromVertex, offsetFromVertex)
    .let { start ->
      drawLine(
        start = start.translateX(width / 2),
        end = start.translateX(-cornerLength),
        color = option.frameColor,
        alpha = option.frameAlpha,
        strokeWidth = width
      )
      drawLine(
        start = start,
        end = start.translateY(cornerLength),
        color = option.frameColor,
        alpha = option.frameAlpha,
        strokeWidth = width
      )
    }
  rect.bottomLeft
    .translate(offsetFromVertex, -offsetFromVertex)
    .let { start ->
      drawLine(
        start = start.translateX(-width / 2),
        end = start.translateX(cornerLength),
        color = option.frameColor,
        alpha = option.frameAlpha,
        strokeWidth = width
      )
      drawLine(
        start = start,
        end = start.translateY(-cornerLength),
        color = option.frameColor,
        alpha = option.frameAlpha,
        strokeWidth = width
      )
    }
  rect.bottomRight
    .translate(-offsetFromVertex, -offsetFromVertex)
    .let { start ->
      drawLine(
        start = start.translateX(width / 2),
        end = start.translateX(-cornerLength),
        color = option.frameColor,
        alpha = option.frameAlpha,
        strokeWidth = width
      )
      drawLine(
        start = start,
        end = start.translateY(-cornerLength),
        color = option.frameColor,
        alpha = option.frameAlpha,
        strokeWidth = width
      )
    }
}

private fun DrawScope.drawGrid(
  offset: Offset,
  size: Size,
  option: CropifyOption,
) {
  val rect = Rect(offset, size)
  val width = option.gridWidth.toPx()
  val widthOneThird = size.width / 3
  val widthTwoThird = widthOneThird * 2
  drawLine(
    color = option.gridColor,
    alpha = option.gridAlpha,
    start = rect.topLeft.translateX(widthOneThird),
    end = rect.bottomLeft.translateX(widthOneThird),
    strokeWidth = width
  )
  drawLine(
    color = option.gridColor,
    alpha = option.gridAlpha,
    start = rect.topLeft.translateX(widthTwoThird),
    end = rect.bottomLeft.translateX(widthTwoThird),
    strokeWidth = width
  )
  val heightOneThird = size.height / 3
  val heightTwoThird = heightOneThird * 2
  drawLine(
    color = option.gridColor,
    alpha = option.gridAlpha,
    start = rect.topLeft.translateY(heightOneThird),
    end = rect.topRight.translateY(heightOneThird),
    strokeWidth = width
  )
  drawLine(
    color = option.gridColor,
    alpha = option.gridAlpha,
    start = rect.topLeft.translateY(heightTwoThird),
    end = rect.topRight.translateY(heightTwoThird),
    strokeWidth = width
  )
}
