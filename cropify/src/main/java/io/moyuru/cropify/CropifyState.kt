package io.moyuru.cropify

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

@Composable
fun rememberCropifyState() = remember(Unit) { CropifyState() }

class CropifyState {
  internal var frameRect by mutableStateOf(Rect(0f, 0f, 0f, 0f))
  internal var imageRect by mutableStateOf(Rect(0f, 0f, 0f, 0f))
  internal var shouldCrop by mutableStateOf(false)
  internal var loadedUri: Uri? = null
  internal var inSampleSize = 1

  fun crop() {
    shouldCrop = true
  }

  internal fun translateFrameRect(offset: Offset) {
    var newRect = frameRect.translate(offset)
    if (newRect.left < imageRect.left) newRect = newRect.translate(imageRect.left - newRect.left, 0f)
    if (newRect.right > imageRect.right) newRect = newRect.translate(imageRect.right - newRect.right, 0f)
    if (newRect.top < imageRect.top) newRect = newRect.translate(0f, imageRect.top - newRect.top)
    if (newRect.bottom > imageRect.bottom) newRect = newRect.translate(0f, imageRect.bottom - newRect.bottom)
    frameRect = newRect
  }

  internal fun scaleFrameRect(
    point: TouchRegion.Vertex,
    aspectRatio: AspectRatio?,
    amount: Offset,
    minimumVertexDistance: Float
  ) {
    frameRect = if (aspectRatio == null) scaleFlexibleRect(point, amount, minimumVertexDistance)
    else scaleFixedAspectRatioRect(point, amount, minimumVertexDistance)
  }

  private fun scaleFlexibleRect(
    point: TouchRegion.Vertex,
    amount: Offset,
    minimumVertexDistance: Float
  ): Rect {
    return frameRect.run {
      val newLeft = (left + amount.x).adjustLeft(minimumVertexDistance)
      val newTop = (top + amount.y).adjustTop(minimumVertexDistance)
      val newRight = (right + amount.x).adjustRight(minimumVertexDistance)
      val newBottom = (bottom + amount.y).adjustBottom(minimumVertexDistance)
      when (point) {
        TouchRegion.Vertex.TOP_LEFT -> Rect(newLeft, newTop, right, bottom)
        TouchRegion.Vertex.TOP_RIGHT -> Rect(left, newTop, newRight, bottom)
        TouchRegion.Vertex.BOTTOM_LEFT -> Rect(newLeft, top, right, newBottom)
        TouchRegion.Vertex.BOTTOM_RIGHT -> Rect(left, top, newRight, newBottom)
      }
    }
  }

  private fun scaleFixedAspectRatioRect(
    point: TouchRegion.Vertex,
    amount: Offset,
    minimumVertexDistance: Float
  ): Rect {
    val a: Float // a = (y2 - y1) / (x2 - x1)
    val b: Float // b = (x2y1 - x1y2) / (x2 - x1)
    when (point) {
      TouchRegion.Vertex.TOP_LEFT, TouchRegion.Vertex.BOTTOM_RIGHT -> {
        a = frameRect.run { (bottom - top) / (right - left) }
        b = frameRect.run { (right * top - left * bottom) / (right - left) }
      }
      else -> {
        a = frameRect.run { (top - bottom) / (right - left) }
        b = frameRect.run { (right * bottom - left * top) / (right - left) }
      }
    }
    val calculateY = { x: Float -> a * x + b }
    val calculateX = { y: Float -> (y - b) / a }
    return when (point) {
      TouchRegion.Vertex.TOP_LEFT -> {
        val left = (frameRect.left + amount.x).adjustLeft(minimumVertexDistance)
        val top = calculateY(left).adjustTop(minimumVertexDistance)
        frameRect.copy(left = calculateX(top), top = top)
      }
      TouchRegion.Vertex.TOP_RIGHT -> {
        val right = (frameRect.right + amount.x).adjustRight(minimumVertexDistance)
        val top = calculateY(right).adjustTop(minimumVertexDistance)
        frameRect.copy(right = calculateX(top), top = top)
      }
      TouchRegion.Vertex.BOTTOM_LEFT -> {
        val left = (frameRect.left + amount.x).adjustLeft(minimumVertexDistance)
        val bottom = calculateY(left).adjustBottom(minimumVertexDistance)
        frameRect.copy(left = calculateX(bottom), bottom = bottom)
      }
      TouchRegion.Vertex.BOTTOM_RIGHT -> {
        val right = (frameRect.right + amount.x).adjustRight(minimumVertexDistance)
        val bottom = calculateY(right).adjustBottom(minimumVertexDistance)
        frameRect.copy(right = calculateX(bottom), bottom = bottom)
      }
    }
  }

  private fun Float.adjustLeft(minimumVertexDistance: Float) =
    coerceAtLeast(imageRect.left).coerceAtMost(frameRect.right - minimumVertexDistance)

  private fun Float.adjustTop(minimumVertexDistance: Float) =
    coerceAtLeast(imageRect.top).coerceAtMost(frameRect.bottom - minimumVertexDistance)

  private fun Float.adjustRight(minimumVertexDistance: Float) =
    coerceAtMost(imageRect.right).coerceAtLeast(frameRect.left + minimumVertexDistance)

  private fun Float.adjustBottom(minimumVertexDistance: Float) =
    coerceAtMost(imageRect.bottom).coerceAtLeast(frameRect.top + minimumVertexDistance)
}