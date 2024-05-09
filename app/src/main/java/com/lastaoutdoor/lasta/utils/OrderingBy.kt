package com.lastaoutdoor.lasta.utils

import android.content.Context
import androidx.annotation.StringRes
import com.lastaoutdoor.lasta.R

enum class OrderingBy(@StringRes val orderText: Int) {
  DISTANCE(R.string.distance_asc),
  RATING(R.string.rating),
  POPULARITY(R.string.popularity),
  DIFFICULTYASCENDING(R.string.difficulty_asc),
  DIFFICULTYDESCENDING(R.string.difficulty_desc);

  fun resourcesToString(context: Context): String {
    return context.getString(this.orderText)
  }
}
