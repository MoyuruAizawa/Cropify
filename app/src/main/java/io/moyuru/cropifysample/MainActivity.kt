package io.moyuru.cropifysample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.view.WindowCompat
import io.moyuru.cropify.Cropify
import io.moyuru.cropify.CropifyOption
import io.moyuru.cropify.rememberCropifyState
import io.moyuru.cropifysample.ui.theme.CropifySampleTheme
import io.moyuru.cropifysample.widget.AspectRatioPicker
import io.moyuru.cropifysample.widget.ColorPicker
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      CropifySampleTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          Screen()
        }
      }
    }
  }

  @Composable
  private fun Screen() {
    val res = LocalContext.current.resources
    val scaffoldState = rememberBottomSheetScaffoldState()
    var image by remember { mutableStateOf(ImageBitmap.imageResource(res, R.drawable.image_sample_landscape)) }
    val cropifyState = rememberCropifyState()
    var cropifyOption by remember { mutableStateOf(CropifyOption()) }
    var croppedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
      topBar = { AppBar(scaffoldState) },
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
        CropifyOptionSelector(
          option = cropifyOption,
          onOptionChanged = { cropifyOption = it },
          onImageSelected = {
            image = it
            coroutineScope.launch { scaffoldState.bottomSheetState.collapse() }
          },
          modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
        )
      },
      floatingActionButton = {
        FloatingActionButton(
          onClick = { cropifyState.crop() },
          modifier = Modifier.navigationBarsPadding()
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_crop),
            contentDescription = stringResource(id = R.string.crop_alt)
          )
        }
      },
      sheetPeekHeight = 0.dp,
      scaffoldState = scaffoldState,
      sheetBackgroundColor = MaterialTheme.colorScheme.surface,
      sheetContentColor = contentColorFor(MaterialTheme.colorScheme.surface),
      backgroundColor = MaterialTheme.colorScheme.background,
      contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.background),
      modifier = Modifier.fillMaxSize()
    )

    croppedImage?.let {
      Dialog(onDismissRequest = { croppedImage = null }) {
        Image(bitmap = it, contentDescription = stringResource(id = R.string.cropped_image_alt))
      }
    }
  }

  @Composable
  private fun AppBar(scaffoldState: BottomSheetScaffoldState, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    TopAppBar(
      title = { Text(text = stringResource(id = R.string.app_name)) },
      actions = {
        IconButton(
          onClick = {
            coroutineScope.launch {
              when {
                scaffoldState.bottomSheetState.isExpanded -> scaffoldState.bottomSheetState.collapse()
                scaffoldState.bottomSheetState.isCollapsed -> scaffoldState.bottomSheetState.expand()
              }
            }
          }
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_setting),
            contentDescription = stringResource(id = R.string.config_alt),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
          )
        }
      },
      modifier = modifier
    )
  }

  @Composable
  private fun CropifyOptionSelector(
    option: CropifyOption,
    onOptionChanged: (CropifyOption) -> Unit,
    onImageSelected: (ImageBitmap) -> Unit,
    modifier: Modifier = Modifier
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
      HeadlineS(text = stringResource(id = R.string.frame))
      TitleM(text = stringResource(id = R.string.frame_color))
      ColorPicker(selectedColor = option.frameColor) { onOptionChanged(option.copy(frameColor = it)) }
      TitleM(text = stringResource(id = R.string.frame_alpha))
      Slider(value = option.frameAlpha, onValueChange = { onOptionChanged(option.copy(frameAlpha = it)) })
      TitleM(text = stringResource(id = R.string.frame_width))
      Slider(
        value = option.frameWidth.value,
        onValueChange = { onOptionChanged(option.copy(frameWidth = it.dp)) },
        valueRange = 1f..12f
      )
      TitleM(text = stringResource(id = R.string.frame_aspect_ratio))
      AspectRatioPicker(selectedAspectRatio = option.frameAspectRatio) {
        onOptionChanged(option.copy(frameAspectRatio = it))
      }

      Space(dp = 16.dp)

      HeadlineS(text = stringResource(id = R.string.grid))
      TitleM(text = stringResource(id = R.string.grid_color))
      ColorPicker(selectedColor = option.gridColor) { onOptionChanged(option.copy(gridColor = it)) }
      TitleM(text = stringResource(id = R.string.grid_alpha))
      Slider(value = option.gridAlpha, onValueChange = { onOptionChanged(option.copy(gridAlpha = it)) })
      TitleM(text = stringResource(id = R.string.grid_width))
      Slider(
        value = option.gridWidth.value,
        onValueChange = { onOptionChanged(option.copy(gridWidth = it.dp)) },
        valueRange = 1f..12f
      )

      Space(dp = 16.dp)

      HeadlineS(text = stringResource(id = R.string.mask))
      TitleM(text = stringResource(id = R.string.mask_color))
      ColorPicker(selectedColor = option.maskColor) { onOptionChanged(option.copy(maskColor = it)) }
      TitleM(text = stringResource(id = R.string.mask_alpha))
      Slider(value = option.maskAlpha, onValueChange = { onOptionChanged(option.copy(maskAlpha = it)) })

      Space(dp = 16.dp)

      HeadlineS(text = stringResource(id = R.string.background))
      TitleM(text = stringResource(id = R.string.background_color))
      ColorPicker(selectedColor = option.backgroundColor) { onOptionChanged(option.copy(backgroundColor = it)) }

      Space(16.dp)

      HeadlineS(text = stringResource(id = R.string.bitmap))
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        val landscape = ImageBitmap.imageResource(id = R.drawable.image_sample_landscape)
        val portrait = ImageBitmap.imageResource(id = R.drawable.image_sample_portrait)
        Box(
          modifier = Modifier
            .weight(1f)
            .height(200.dp)
            .clickable { onImageSelected(landscape) }
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
            .clickable { onImageSelected(portrait) }
        ) {
          Image(
            bitmap = portrait,
            contentDescription = null,
            modifier = Modifier.matchParentSize()
          )
        }
      }
    }
  }

  @Composable
  private fun HeadlineS(text: String) {
    Text(text = text, style = MaterialTheme.typography.headlineSmall)
  }

  @Composable
  private fun TitleM(text: String) {
    Text(text = text, style = MaterialTheme.typography.titleMedium)
  }

  @Composable
  private fun ColumnScope.Space(dp: Dp) {
    Spacer(modifier = Modifier.height(dp))
  }
}
