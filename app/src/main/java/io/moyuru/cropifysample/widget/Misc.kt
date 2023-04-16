package io.moyuru.cropifysample.widget

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun HeadlineS(text: String) {
  Text(text = text, style = MaterialTheme.typography.headlineSmall)
}

@Composable
fun TitleM(text: String) {
  Text(text = text, style = MaterialTheme.typography.titleMedium)
}

@Composable
fun ColumnScope.Space(dp: Dp) {
  Spacer(modifier = Modifier.height(dp))
}