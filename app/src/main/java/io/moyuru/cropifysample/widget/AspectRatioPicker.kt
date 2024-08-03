package io.moyuru.cropifysample.widget

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.moyuru.cropify.CropifySize
import io.moyuru.cropifysample.R

@Composable
fun AspectRatioPicker(
  selectedFixedAspectRatio: CropifySize.FixedAspectRatio?,
  modifier: Modifier = Modifier,
  onPicked: (CropifySize.FixedAspectRatio?) -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
  ) {
    val aspectRatioList = listOf(
      4 to 3,
      16 to 9,
      1 to 1,
      9 to 16,
      3 to 4,
    )
    aspectRatioList.forEach { (x, y) ->
      val fixedAspectRatio = CropifySize.FixedAspectRatio(x, y)
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
          .clickable { onPicked(fixedAspectRatio) }
          .padding(8.dp)
      ) {
        Crossfade(
          targetState = if (selectedFixedAspectRatio == fixedAspectRatio)
            MaterialTheme.colorScheme.primary
          else
            MaterialTheme.colorScheme.onSurface
        ) { color ->
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(40.dp)
          ) {
            Spacer(
              modifier = Modifier
                .aspectRatio(x / y.toFloat())
                .border(2.dp, color)
            )
          }
        }
        Text(text = "$x:$y")
      }
    }
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(4.dp),
      modifier = Modifier
        .clickable { onPicked(null) }
        .padding(8.dp)
    ) {
      Spacer(modifier = Modifier.size(40.dp))
      Text(text = stringResource(id = R.string.flexible))
    }
  }
}
