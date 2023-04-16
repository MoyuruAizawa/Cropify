package io.moyuru.cropifysample.screen

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.moyuru.cropifysample.LocalNavigator
import io.moyuru.cropifysample.R

@Composable
fun MenuScreen() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .navigationBarsPadding()
      .statusBarsPadding()
  ) {
    val navigator = LocalNavigator.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      if (it.resultCode == Activity.RESULT_OK) {
        it.data?.data?.let { navigator.navigateToFile(it) }
      }
    }
    Menu(stringResource(id = R.string.crop_bitmap)) { navigator.navigateToBitmap() }
    Menu(stringResource(id = R.string.crop_file)) {
      launcher.launch(
        Intent.createChooser(
          Intent(Intent.ACTION_OPEN_DOCUMENT)
            .apply {
              addCategory(Intent.CATEGORY_OPENABLE)
              type = "images/*"
              putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*"))
            },
          null
        )
      )
    }
  }
}

@Composable
private fun Menu(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
  Text(
    text = text,
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(16.dp)
  )
}