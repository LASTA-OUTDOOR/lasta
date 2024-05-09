package com.lastaoutdoor.lasta.models.activity

import android.content.Context
import androidx.annotation.StringRes
import com.lastaoutdoor.lasta.R

enum class ActivityType(@StringRes val activityText: Int) {
  CLIMBING(R.string.climbing),
  HIKING(R.string.hiking),
  BIKING(R.string.biking);

  fun resourcesToString(context: Context): String {
    return context.getString(this.activityText)
  }
}
