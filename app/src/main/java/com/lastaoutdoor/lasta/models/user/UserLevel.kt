package com.lastaoutdoor.lasta.models.user

import android.content.Context
import androidx.annotation.StringRes
import com.lastaoutdoor.lasta.R

/** Enum class for hiking levels */
enum class UserLevel(@StringRes val levelText: Int) {
  BEGINNER(R.string.beginner),
  INTERMEDIATE(R.string.intermediate),
  ADVANCED(R.string.advanced);

  fun resourcesToString(context: Context): String {
    return context.getString(this.levelText)
  }
}

data class UserActivitiesLevel(
    val climbingLevel: UserLevel,
    val hikingLevel: UserLevel,
    val bikingLevel: UserLevel
)
