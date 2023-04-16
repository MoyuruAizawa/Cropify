package io.moyuru.cropifysample.widget

import androidx.compose.foundation.Image
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import io.moyuru.cropifysample.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun AppBar(title: String, bottomSheetState: BottomSheetState, modifier: Modifier = Modifier) {
  TopAppBar(
    navigationIcon = {
      Image(
        painter = painterResource(id = R.drawable.ic_navigate),
        contentDescription = stringResource(id = R.string.navigation_button_alt),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
      )
    },
    title = { Text(text = title) },
    actions = {
      val coroutineScope = rememberCoroutineScope()
      IconButton(
        onClick = {
          coroutineScope.launch {
            when {
              bottomSheetState.isExpanded -> bottomSheetState.collapse()
              bottomSheetState.isCollapsed -> bottomSheetState.expand()
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