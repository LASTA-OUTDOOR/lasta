package com.lastaoutdoor.lasta.models.user

import android.content.Context
import androidx.annotation.StringRes
import com.lastaoutdoor.lasta.R

enum class Language(@StringRes val languageText: Int) {
  ENGLISH(R.string.english),
  FRENCH(R.string.french),
  GERMAN(R.string.german);

  fun resourcesToString(context: Context): String {
    return context.getString(this.languageText)
  }


}
