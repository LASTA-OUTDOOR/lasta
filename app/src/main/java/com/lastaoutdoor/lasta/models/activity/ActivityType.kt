package com.lastaoutdoor.lasta.models.activity

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.lastaoutdoor.lasta.R

enum class ActivityType(@StringRes val activityText: Int) {
  CLIMBING(R.string.climbing),
  HIKING(R.string.hiking),
  BIKING(R.string.biking);

  fun resourcesToString(context: Context): String {
    return context.getString(this.activityText)
  }

  @Composable
  fun getIcon(): Painter {
    return when (this) {
      CLIMBING -> painterResource(R.drawable.climbing_roundicon)
      HIKING -> painterResource(R.drawable.hiking_roundicon)
      BIKING -> painterResource(R.drawable.biking_roundicon)
    }
  }
}
