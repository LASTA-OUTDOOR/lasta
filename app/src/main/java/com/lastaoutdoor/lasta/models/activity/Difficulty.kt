package com.lastaoutdoor.lasta.models.activity

import androidx.compose.ui.graphics.Color
import com.lastaoutdoor.lasta.ui.theme.GreenDifficulty
import com.lastaoutdoor.lasta.ui.theme.OrangeDifficulty
import com.lastaoutdoor.lasta.ui.theme.RedDifficulty

enum class Difficulty {
  EASY,
  NORMAL,
  HARD;

  override fun toString(): String {
    return name.lowercase().replaceFirstChar { it.uppercase() }
  }

  fun getColorByDifficulty(): Color {
    return when (this) {
      EASY -> GreenDifficulty
      NORMAL -> OrangeDifficulty
      HARD -> RedDifficulty
    }
  }
}
