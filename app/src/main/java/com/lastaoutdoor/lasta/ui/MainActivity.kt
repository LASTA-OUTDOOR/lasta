package com.lastaoutdoor.lasta.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lastaoutdoor.lasta.ui.navigation.AppNavGraph
import com.lastaoutdoor.lasta.ui.theme.LastaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    requestNotificationPermission()
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    setContent { LastaTheme { AppNavGraph() } }
  }

  private fun requestNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      val hasPermission =
          ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
              PackageManager.PERMISSION_GRANTED

      if (!hasPermission) {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
      }
    }
  }
}
