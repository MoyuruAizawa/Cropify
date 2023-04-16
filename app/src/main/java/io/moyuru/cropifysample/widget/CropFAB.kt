package io.moyuru.cropifysample.widget

import androidx.compose.foundation.Image
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import io.moyuru.cropifysample.R

@Composable
fun CropFAB(modifier: Modifier = Modifier, onClick: () -> Unit) {
  FloatingActionButton(
    onClick = onClick,
    modifier = modifier
  ) {
    Image(
      painter = painterResource(id = R.drawable.ic_crop),
      contentDescription = stringResource(id = R.string.crop_alt)
    )
  }
}