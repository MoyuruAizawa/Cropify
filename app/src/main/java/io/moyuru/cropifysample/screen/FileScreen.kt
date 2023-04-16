package io.moyuru.cropifysample.screen

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.moyuru.cropify.Cropify
import io.moyuru.cropify.CropifyOption
import io.moyuru.cropify.rememberCropifyState
import io.moyuru.cropifysample.R
import io.moyuru.cropifysample.widget.AppBar
import io.moyuru.cropifysample.widget.CropFAB
import io.moyuru.cropifysample.widget.CropifyOptionSelector
import io.moyuru.cropifysample.widget.ImagePreviewDialog

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FileScreen(imageUri: Uri) {
  val scaffoldState = rememberBottomSheetScaffoldState()
  val cropifyState = rememberCropifyState()
  var cropifyOption by remember { mutableStateOf(CropifyOption()) }
  var croppedImage by remember { mutableStateOf<ImageBitmap?>(null) }

  croppedImage?.let { ImagePreviewDialog(bitmap = it) { croppedImage = null } }
  BottomSheetScaffold(
    topBar = {
      AppBar(
        title = stringResource(id = R.string.crop_file),
        bottomSheetState = scaffoldState.bottomSheetState
      )
    },
    content = { it ->
      val context = LocalContext.current
      Cropify(
        uri = imageUri,
        state = cropifyState,
        option = cropifyOption,
        onImageCropped = { croppedImage = it },
        onFailedToLoadImage = { Toast.makeText(context, R.string.failed_load_image, Toast.LENGTH_SHORT).show() },
        modifier = Modifier
          .fillMaxSize()
          .padding(it)
      )
    },
    sheetContent = {
      CropifyOptionSelector(
        option = cropifyOption,
        onOptionChanged = { cropifyOption = it },
        modifier = Modifier
          .verticalScroll(rememberScrollState())
          .padding(16.dp)
          .statusBarsPadding()
          .navigationBarsPadding()
      )
    },
    floatingActionButton = {
      CropFAB(modifier = Modifier.navigationBarsPadding()) { cropifyState.crop() }
    },
    sheetPeekHeight = 0.dp,
    scaffoldState = scaffoldState,
    sheetBackgroundColor = MaterialTheme.colorScheme.surface,
    sheetContentColor = contentColorFor(MaterialTheme.colorScheme.surface),
    backgroundColor = MaterialTheme.colorScheme.background,
    contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.background),
    modifier = Modifier.fillMaxSize()
  )
}