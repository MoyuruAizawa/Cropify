package io.moyuru.cropify

import androidx.compose.ui.unit.IntSize
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
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

  @Test
  fun decodeSampledImage_closesAllStreams_onSuccess() {
    val opener = StreamOpener()
    var decodedSampleSize = 0
    val result = decodeSample(opener, decode = { stream, sampleSize ->
      assertSame(opener.opened[2], stream)
      decodedSampleSize = sampleSize
      "bitmap"
    })
    assertEquals("bitmap", result.bitmap)
    assertEquals(6, result.orientation)
    assertEquals(calculateInSampleSize(IntSize(2160, 3840), IntSize(1080, 1920)), result.inSampleSize)
    assertEquals(2, decodedSampleSize)
    assertClosed(opener, attempts = 3, streams = 3)
  }

  @Test
  fun decodeSampledImage_closesAllStreams_whenDecodeReturnsNull() {
    val opener = StreamOpener()
    val error = assertThrows(IOException::class.java) {
      decodeSample(opener, decode = { _, _ -> null })
    }
    assertEquals("Failed to decode stream.", error.message)
    assertClosed(opener, attempts = 3, streams = 3)
  }

  @Test
  fun decodeSampledImage_closesStreams_whenDecodeThrows() {
    val opener = StreamOpener()
    val failure = RuntimeException("decode failed")
    val error = assertThrows(RuntimeException::class.java) {
      decodeSample(opener, decode = { _, _ -> throw failure })
    }
    assertSame(failure, error)
    assertClosed(opener, attempts = 3, streams = 3)
  }

  @Test
  fun decodeSampledImage_closesBoundsStream_whenReadBoundsThrows() {
    val opener = StreamOpener()
    val failure = IOException("bounds failed")
    val error = assertThrows(IOException::class.java) {
      decodeSample(opener, readBounds = { throw failure })
    }
    assertSame(failure, error)
    assertClosed(opener, attempts = 1, streams = 1)
  }

  @Test
  fun decodeSampledImage_closesExifStream_whenReadOrientationThrows() {
    val opener = StreamOpener()
    val failure = IOException("EXIF failed")
    val error = assertThrows(IOException::class.java) {
      decodeSample(opener, readOrientation = { throw failure })
    }
    assertSame(failure, error)
    assertClosed(opener, attempts = 2, streams = 2)
  }

  @Test
  fun decodeSampledImage_throwsWithoutOpeningBody_whenExifStreamIsNull() {
    val opener = StreamOpener(nullOnOpen = setOf(1))
    val error = assertThrows(IOException::class.java) { decodeSample(opener) }
    assertEquals("Failed to open input stream.", error.message)
    assertClosed(opener, attempts = 2, streams = 1)
  }

  @Test
  fun decodeSampledImage_closesPreviousStreams_whenOpenThrows() {
    val opener = StreamOpener(failOnOpen = setOf(2))
    val error = assertThrows(IOException::class.java) { decodeSample(opener) }
    assertEquals("open failed #2", error.message)
    assertClosed(opener, attempts = 3, streams = 2)
  }

  @Test
  fun decodeSampledImage_passesNullStreamToDecoders() {
    val opener = StreamOpener(nullOnOpen = setOf(0, 2))
    var boundsCalled = false
    var decodeCalled = false
    val result = decodeSample(
      opener,
      readBounds = {
        assertNull(it)
        boundsCalled = true
        IntSize(2160, 3840)
      },
      decode = { stream, _ ->
        assertNull(stream)
        decodeCalled = true
        "bitmap"
      }
    )
    assertTrue(boundsCalled)
    assertTrue(decodeCalled)
    assertEquals("bitmap", result.bitmap)
    assertClosed(opener, attempts = 3, streams = 1)
  }

  @Test
  fun decodeFullImage_closesStream_onSuccess() {
    val opener = StreamOpener()
    assertEquals("bitmap", decodeFullImage(opener::open) {
      assertSame(opener.opened.single(), it)
      "bitmap"
    })
    assertClosed(opener, attempts = 1, streams = 1)
  }

  @Test
  fun decodeFullImage_closesStream_whenDecodeReturnsNull() {
    val opener = StreamOpener()
    assertNull(decodeFullImage<String>(opener::open) { null })
    assertClosed(opener, attempts = 1, streams = 1)
  }

  @Test
  fun decodeFullImage_closesStream_whenDecodeThrows() {
    val opener = StreamOpener()
    val failure = RuntimeException("decode failed")
    val error = assertThrows(RuntimeException::class.java) {
      decodeFullImage<String>(opener::open) { throw failure }
    }
    assertSame(failure, error)
    assertClosed(opener, attempts = 1, streams = 1)
  }

  @Test
  fun decodeFullImage_returnsNull_whenStreamIsNull() {
    val opener = StreamOpener(nullOnOpen = setOf(0))
    var decodeCalled = false
    assertNull(decodeFullImage<String>(opener::open) {
      assertNull(it)
      decodeCalled = true
      null
    })
    assertTrue(decodeCalled)
    assertClosed(opener, attempts = 1, streams = 0)
  }

  private fun decodeSample(
    opener: StreamOpener,
    readBounds: (InputStream?) -> IntSize = { IntSize(2160, 3840) },
    readOrientation: (InputStream) -> Int = { 6 },
    decode: (InputStream?, Int) -> String? = { _, _ -> "bitmap" }
  ): DecodedSample<String> = decodeSampledImage(
    opener::open, IntSize(1080, 1920), readBounds, readOrientation, decode
  )

  private fun assertClosed(opener: StreamOpener, attempts: Int, streams: Int) {
    assertEquals(attempts, opener.attemptCount)
    assertEquals(streams, opener.opened.size)
    assertTrue(opener.opened.all { it.closeCount == 1 })
    assertEquals(if (streams == 0) 0 else 1, opener.maxConcurrentOpen)
  }
}

private class TrackingInputStream : ByteArrayInputStream(ByteArray(0)) {
  var closeCount = 0
    private set
  val isClosed get() = closeCount > 0

  override fun close() {
    closeCount++
    super.close()
  }
}

private class StreamOpener(
  private val failOnOpen: Set<Int> = emptySet(),
  private val nullOnOpen: Set<Int> = emptySet()
) {
  val opened = mutableListOf<TrackingInputStream>()
  var attemptCount = 0
    private set
  var maxConcurrentOpen = 0
    private set

  fun open(): InputStream? {
    val index = attemptCount++
    if (index in failOnOpen) throw IOException("open failed #$index")
    if (index in nullOnOpen) return null
    return TrackingInputStream().also {
      opened += it
      maxConcurrentOpen = maxOf(maxConcurrentOpen, opened.count { stream -> !stream.isClosed })
    }
  }
}
