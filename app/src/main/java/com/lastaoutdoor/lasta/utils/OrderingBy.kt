package com.lastaoutdoor.lasta.utils

import androidx.annotation.StringRes
import com.lastaoutdoor.lasta.R

enum class OrderingBy(@StringRes val orderText: Int) {
  DISTANCEASCENDING(R.string.distance_asc),
  DISTANCEDESCENDING(R.string.distance_desc),
  RATING(R.string.rating),
  POPULARITY(R.string.popularity),
  DIFFICULTYASCENDING(R.string.difficulty_asc),
  DIFFICULTYDESCENDING(R.string.difficulty_desc)
}
