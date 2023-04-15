package io.moyuru.cropify

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

internal class CropifyTest {

  @Test
  fun calculateImagePosition() {
    val bitmap = mockk<ImageBitmap>()
    every { bitmap.width } returns 1920
    every { bitmap.height } returns 1080
    val canvasSize = Size(1080f, 1920f)
    val imageSize = calculateImageSize(bitmap, canvasSize)

    assertEquals(
      Rect(
        Offset(
          (canvasSize.width - imageSize.width) / 2f,
          (canvasSize.height - imageSize.height) / 2f
        ),
        imageSize
      ),
      calculateImagePosition(
        bitmap = bitmap,
        canvasSize = canvasSize
      )
    )
  }

  @Test
  fun calculateImageSize() {
    val bitmap = mockk<ImageBitmap>()

    every { bitmap.width } returns 3024
    every { bitmap.height } returns 4032
    assertEquals(
      Size(1080f, 1440f),
      calculateImageSize(bitmap = bitmap, canvasSize = Size(1080f, 1920f))
    )

    every { bitmap.width } returns 4032
    every { bitmap.height } returns 3024
    assertEquals(
      Size(1080f, 810f),
      calculateImageSize(bitmap = bitmap, canvasSize = Size(1080f, 1920f)),
    )

    every { bitmap.width } returns 3024
    every { bitmap.height } returns 4032
    assertEquals(
      Size(810f, 1080f),
      calculateImageSize(bitmap = bitmap, canvasSize = Size(1920f, 1080f))
    )

    every { bitmap.width } returns 4032
    every { bitmap.height } returns 3024
    assertEquals(
      Size(1440f, 1080f),
      calculateImageSize(bitmap = bitmap, canvasSize = Size(1920f, 1080f)),
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