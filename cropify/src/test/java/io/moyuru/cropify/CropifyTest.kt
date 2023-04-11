package io.moyuru.cropify

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import org.junit.Assert.assertEquals
import org.junit.Test

internal class CropifyTest {

  @Test
  fun calculateImagePosition() {
    assertEquals(
      Rect(50f, 100f, 250f, 300f),
      calculateImagePosition(
        imageSize = Size(200f, 200f),
        canvasSize = Size(300f, 400f)
      )
    )
  }

  @Test
  fun calculateImageSize() {
    assertEquals(
      Size(1080f, 1920f),
      calculateImageSize(
        bitmapWidth = 1920,
        bitmapHeight = 1080,
        canvasSize = Size(1920f, 1080f)
      )
    )

    assertEquals(
      Size(1080f, 607.5f),
      calculateImageSize(
        bitmapWidth = 1080,
        bitmapHeight = 1920,
        canvasSize = Size(1920f, 1080f)
      ),
    )
  }

  @Test
  fun detectTouchRegion() {
    assertEquals(
      TouchRegion.Vertex.TOP_LEFT,
      detectTouchRegion(
        tapPosition = Offset(0f, 0f),
        frameRect = Rect(0f, 0f, 100f, 100f),
        tolerance = 2f
      )
    )
    assertEquals(
      TouchRegion.Vertex.TOP_LEFT,
      detectTouchRegion(
        tapPosition = Offset(1f, 1f),
        frameRect = Rect(0f, 0f, 100f, 100f),
        tolerance = 2f
      )
    )
    assertEquals(
      TouchRegion.Vertex.TOP_LEFT,
      detectTouchRegion(
        tapPosition = Offset(-1f, -1f),
        frameRect = Rect(0f, 0f, 100f, 100f),
        tolerance = 2f
      )
    )

    assertEquals(
      TouchRegion.Vertex.TOP_RIGHT,
      detectTouchRegion(
        tapPosition = Offset(100f, 0f),
        frameRect = Rect(0f, 0f, 100f, 100f),
        tolerance = 2f
      )
    )
    assertEquals(
      TouchRegion.Vertex.TOP_RIGHT,
      detectTouchRegion(
        tapPosition = Offset(101f, -1f),
        frameRect = Rect(0f, 0f, 100f, 100f),
        tolerance = 2f
      )
    )
    assertEquals(
      TouchRegion.Vertex.TOP_RIGHT,
      detectTouchRegion(
        tapPosition = Offset(99f, 1f),
        frameRect = Rect(0f, 0f, 100f, 100f),
        tolerance = 2f
      )
    )

    assertEquals(
      TouchRegion.Vertex.BOTTOM_LEFT,
      detectTouchRegion(
        tapPosition = Offset(0f, 100f),
        frameRect = Rect(0f, 0f, 100f, 100f),
        tolerance = 2f
      )
    )
    assertEquals(
      TouchRegion.Vertex.BOTTOM_LEFT,
      detectTouchRegion(
        tapPosition = Offset(1f, 99f),
        frameRect = Rect(0f, 0f, 100f, 100f),
        tolerance = 2f
      )
    )
    assertEquals(
      TouchRegion.Vertex.BOTTOM_LEFT,
      detectTouchRegion(
        tapPosition = Offset(-1f, 101f),
        frameRect = Rect(0f, 0f, 100f, 100f),
        tolerance = 2f
      )
    )

    assertEquals(
      TouchRegion.Vertex.BOTTOM_RIGHT,
      detectTouchRegion(
        tapPosition = Offset(100f, 100f),
        frameRect = Rect(0f, 0f, 100f, 100f),
        tolerance = 2f
      )
    )
    assertEquals(
      TouchRegion.Vertex.BOTTOM_RIGHT,
      detectTouchRegion(
        tapPosition = Offset(99f, 99f),
        frameRect = Rect(0f, 0f, 100f, 100f),
        tolerance = 2f
      )
    )
    assertEquals(
      TouchRegion.Vertex.BOTTOM_RIGHT,
      detectTouchRegion(
        tapPosition = Offset(101f, 101f),
        frameRect = Rect(0f, 0f, 100f, 100f),
        tolerance = 2f
      )
    )

    assertEquals(
      TouchRegion.Inside,
      detectTouchRegion(
        tapPosition = Offset(50f, 50f),
        frameRect = Rect(0f, 0f, 100f, 100f),
        tolerance = 2f
      )
    )

    assertEquals(
      null,
      detectTouchRegion(
        tapPosition = Offset(102f, 102f),
        frameRect = Rect(0f, 0f, 100f, 100f),
        tolerance = 2f
      )
    )
  }
}