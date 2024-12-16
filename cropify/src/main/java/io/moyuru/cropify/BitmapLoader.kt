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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal suspend fun loadImageBitmap(context: Context, uri: Uri): ImageBitmap? {
  return withContext(Dispatchers.IO) {
    val resolver = context.contentResolver
    BitmapFactory.decodeStream(resolver.openInputStream(uri), Rect(), BitmapFactory.Options())?.asImageBitmap()
  }
}

internal suspend fun loadSampledImageBitmap(context: Context, uri: Uri, requireSize: IntSize): SampledImageBitmap {
  return withContext(Dispatchers.IO) {
    val resolver = context.contentResolver
    val options = BitmapFactory.Options().apply {
      inJustDecodeBounds = true
      BitmapFactory.decodeStream(resolver.openInputStream(uri), Rect(), this)
    }
    val inSampleSize = calculateInSampleSize(IntSize(options.outWidth, options.outHeight), requireSize)
    options.apply {
      inJustDecodeBounds = false
      this.inSampleSize = inSampleSize
    }

    resolver.openInputStream(uri).use {
      if (it == null) {
        throw IOException("Failed to open input stream.")
      }

      val exif = ExifInterface(it)
      val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

      val bitmap = BitmapFactory.decodeStream(resolver.openInputStream(uri), Rect(), options)
        ?: throw IOException("Failed to decode stream.")

      SampledImageBitmap(
        imageBitmap = applyExifOrientation(bitmap, orientation).asImageBitmap(),
        inSampleSize = inSampleSize
      )
    }
  }
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