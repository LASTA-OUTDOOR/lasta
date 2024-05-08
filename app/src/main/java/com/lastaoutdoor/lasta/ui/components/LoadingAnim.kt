package com.lastaoutdoor.lasta.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun LoadingAnim(width: Int, tag: String = "LoadingBar") {
  Box(modifier = Modifier.fillMaxSize().testTag(tag), contentAlignment = Alignment.Center) {
    CircularProgressIndicator(modifier = Modifier.width(width.dp))
  }
}
