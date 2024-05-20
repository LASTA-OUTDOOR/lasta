package com.lastaoutdoor.lasta.models.user

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.lastaoutdoor.lasta.R

enum class Language(@StringRes val languageText: Int) {
  ENGLISH(R.string.english),
  FRENCH(R.string.french),
  GERMAN(R.string.german);

  fun resourcesToString(context: Context): String {
    return context.getString(this.languageText)
  }

  fun toLocale(): String {
    return when (this) {
      ENGLISH -> "en"
      FRENCH -> "fr"
      GERMAN -> "de"
    }
  }

  @Composable
  fun getIcon(): Painter {
    return when (this) {
      ENGLISH -> painterResource(R.drawable.english_logo)
      FRENCH -> painterResource(R.drawable.french_logo)
      GERMAN -> painterResource(R.drawable.german_logo)
    }
  }
}
