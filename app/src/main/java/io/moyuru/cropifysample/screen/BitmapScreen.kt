package io.moyuru.cropifysample.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.moyuru.cropify.Cropify
import io.moyuru.cropify.CropifyOption
import io.moyuru.cropify.rememberCropifyState
import io.moyuru.cropifysample.R
import io.moyuru.cropifysample.widget.AppBar
import io.moyuru.cropifysample.widget.CropFAB
import io.moyuru.cropifysample.widget.CropifyOptionSelector
import io.moyuru.cropifysample.widget.HeadlineS
import io.moyuru.cropifysample.widget.ImagePreviewDialog
import io.moyuru.cropifysample.widget.Space
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BitmapScreen() {
  val scaffoldState = rememberBottomSheetScaffoldState()
  val cropifyState = rememberCropifyState()
  var cropifyOption by remember { mutableStateOf(CropifyOption()) }
  val res = LocalContext.current.resources
  var image by remember { mutableStateOf(ImageBitmap.imageResource(res, R.drawable.image_sample_landscape)) }
  var croppedImage by remember { mutableStateOf<ImageBitmap?>(null) }

  croppedImage?.let { ImagePreviewDialog(bitmap = it) { croppedImage = null } }
  BottomSheetScaffold(
    topBar = {
      AppBar(
        title = stringResource(id = R.string.crop_bitmap),
        bottomSheetState = scaffoldState.bottomSheetState
      )
    },
    content = {
      Cropify(
        bitmap = image,
        state = cropifyState,
        option = cropifyOption,
        onImageCropped = { croppedImage = it },
        modifier = Modifier
          .padding(it)
          .fillMaxSize()
      )
    },
    sheetContent = {
      Column(
        modifier = Modifier
          .verticalScroll(rememberScrollState())
          .padding(16.dp)
          .statusBarsPadding()
          .navigationBarsPadding()
      ) {
        CropifyOptionSelector(
          option = cropifyOption,
          onOptionChanged = { cropifyOption = it },
        )

        Space(16.dp)

        HeadlineS(text = stringResource(id = R.string.image))
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
          val landscape = ImageBitmap.imageResource(id = R.drawable.image_sample_landscape)
          val portrait = ImageBitmap.imageResource(id = R.drawable.image_sample_portrait)
          val coroutineScope = rememberCoroutineScope()
          Box(
            modifier = Modifier
              .weight(1f)
              .height(200.dp)
              .clickable {
                image = landscape
                coroutineScope.launch { scaffoldState.bottomSheetState.collapse() }
              }
          ) {
            Image(
              bitmap = landscape,
              contentDescription = null,
              modifier = Modifier.matchParentSize()
            )
          }
          Box(
            modifier = Modifier
              .weight(1f)
              .height(200.dp)
              .clickable {
                image = portrait
                coroutineScope.launch { scaffoldState.bottomSheetState.collapse() }
              }
          ) {
            Image(
              bitmap = portrait,
              contentDescription = null,
              modifier = Modifier.matchParentSize()
            )
          }
        }

      }
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
