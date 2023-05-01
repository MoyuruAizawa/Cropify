package io.moyuru.cropify

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Rect
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
    BitmapFactory.decodeStream(resolver.openInputStream(uri), Rect(), options)
      ?.let { SampledImageBitmap(it.asImageBitmap(), inSampleSize) }
      ?: throw IOException("Failed to decode stream.")
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

internal data class SampledImageBitmap(val imageBitmap: ImageBitmap, val inSampleSize: Int)