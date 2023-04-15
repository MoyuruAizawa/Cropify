package io.moyuru.cropify

import androidx.compose.ui.unit.IntSize
import org.junit.Assert.assertEquals
import org.junit.Test

internal class BitmapLoaderTest {

  @Test
  fun calculateInSampleSize() {
    assertEquals(
      1,
      calculateInSampleSize(IntSize(1080, 1920), IntSize(1080, 1920))
    )

    assertEquals(
      1,
      calculateInSampleSize(IntSize(1620, 2880), IntSize(1080, 1920))
    )

    assertEquals(
      2,
      calculateInSampleSize(IntSize(2160, 3840), IntSize(1080, 1920))
    )

    assertEquals(
      2,
      calculateInSampleSize(IntSize(3240, 5760), IntSize(1080, 1920))
    )
  }
}