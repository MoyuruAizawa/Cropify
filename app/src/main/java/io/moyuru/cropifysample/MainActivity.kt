package io.moyuru.cropifysample

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.moyuru.cropifysample.screen.BitmapScreen
import io.moyuru.cropifysample.screen.FileScreen
import io.moyuru.cropifysample.screen.MenuScreen
import io.moyuru.cropifysample.ui.theme.CropifySampleTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      CropifySampleTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          val navController = rememberNavController()
          CompositionLocalProvider(LocalNavigator provides Navigator(navController)) {
            NavHost(
              navController = navController,
              startDestination = Screen.Menu.path,
            ) {
              composable(Screen.Menu.path) { MenuScreen() }
              composable(Screen.Bitmap.path) { BitmapScreen() }
              composable(
                Screen.File.path,
                arguments = listOf(navArgument(Screen.File.argUri) { type = NavType.StringType })
              ) {
                FileScreen(Uri.parse(it.arguments?.getString(Screen.File.argUri)))
              }
            }
          }
        }
      }
    }
  }
}
