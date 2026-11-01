package io.moyuru.cropify

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Rect
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.IntSize
import java.io.IOException
import java.io.InputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal suspend fun loadImageBitmap(context: Context, uri: Uri): ImageBitmap? {
  return withContext(Dispatchers.IO) {
    val resolver = context.contentResolver
    decodeFullImage({ resolver.openInputStream(uri) }) {
      BitmapFactory.decodeStream(it, Rect(), BitmapFactory.Options())
    }?.asImageBitmap()
  }
}

internal suspend fun loadSampledImageBitmap(context: Context, uri: Uri, requireSize: IntSize): SampledImageBitmap {
  return withContext(Dispatchers.IO) {
    val resolver = context.contentResolver
    val options = BitmapFactory.Options()
    val sample = decodeSampledImage(
      openStream = { resolver.openInputStream(uri) },
      requireSize = requireSize,
      readBounds = {
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(it, Rect(), options)
        IntSize(options.outWidth, options.outHeight)
      },
      readOrientation = {
        ExifInterface(it).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
      },
      decode = { stream, inSampleSize ->
        options.inJustDecodeBounds = false
        options.inSampleSize = inSampleSize
        BitmapFactory.decodeStream(stream, Rect(), options)
      }
    )
    SampledImageBitmap(
      imageBitmap = applyExifOrientation(sample.bitmap, sample.orientation).asImageBitmap(),
      inSampleSize = sample.inSampleSize
    )
  }
}

internal fun <B : Any> decodeFullImage(
  openStream: () -> InputStream?,
  decode: (InputStream?) -> B?
): B? = openStream().use(decode)

internal data class DecodedSample<B : Any>(val bitmap: B, val orientation: Int, val inSampleSize: Int)

internal fun <B : Any> decodeSampledImage(
  openStream: () -> InputStream?,
  requireSize: IntSize,
  readBounds: (InputStream?) -> IntSize,
  readOrientation: (InputStream) -> Int,
  decode: (InputStream?, inSampleSize: Int) -> B?
): DecodedSample<B> {
  val imageSize = openStream().use(readBounds)
  val inSampleSize = calculateInSampleSize(imageSize, requireSize)
  val orientation = openStream().use {
    if (it == null) throw IOException("Failed to open input stream.")
    readOrientation(it)
  }
  val bitmap = openStream().use { decode(it, inSampleSize) }
    ?: throw IOException("Failed to decode stream.")
  return DecodedSample(bitmap, orientation, inSampleSize)
}

internal fun calculateInSampleSize(imageSize: IntSize, requireSize: IntSize): Int {
  var inSampleSize = 1
  if (imageSize.height > requireSize.height || imageSize.width > requireSize.width) {
    val halfHeight: Int = imageSize.height / 2
    val halfWidth: Int = imageSize.width / 2
    while (halfHeight / inSampleSize >= requireSize.height && halfWidth / inSampleSize >= requireSize.width) {
      inSampleSize *= 2
    }
  }
  return inSampleSize
}

internal fun applyExifOrientation(bitmap: Bitmap, orientation: Int): Bitmap {
  val matrix = Matrix()
  when (orientation) {
    ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
    ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
    ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
    ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
    ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
    ExifInterface.ORIENTATION_TRANSPOSE -> {
      matrix.postRotate(90f)
      matrix.postScale(-1f, 1f)
    }

    ExifInterface.ORIENTATION_TRANSVERSE -> {
      matrix.postRotate(270f)
      matrix.postScale(-1f, 1f)
    }
  }
  return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

internal data class SampledImageBitmap(val imageBitmap: ImageBitmap, val inSampleSize: Int)