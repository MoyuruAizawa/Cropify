package io.moyuru.cropifysample.widget

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorPicker(selectedColor: Color?, modifier: Modifier = Modifier, onPicked: (Color) -> Unit) {
  Row(modifier = modifier) {
    listOf(
      Color.White,
      Color.Black,
      Color.Red,
      Color.Green,
      Color.Blue,
    ).forEach {
      ColorCard(color = it, isSelected = it == selectedColor) { onPicked(it) }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColorCard(color: Color, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
  Crossfade(targetState = isSelected) {
    Card(
      onClick = onClick,
      shape = RoundedCornerShape(8.dp),
      border = if (it) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
      modifier = modifier
    ) {
      Spacer(
        modifier = Modifier
          .size(32.dp)
          .background(color = color)
      )
    }
  }
}