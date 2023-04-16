package io.moyuru.cropifysample.widget

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import io.moyuru.cropifysample.R

@Composable
fun ImagePreviewDialog(bitmap: ImageBitmap, onDismissRequest: () -> Unit) {
  Dialog(onDismissRequest = onDismissRequest) {
    Image(bitmap = bitmap, contentDescription = stringResource(id = R.string.cropped_image_alt))
  }
}