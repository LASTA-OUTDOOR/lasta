package com.lastaoutdoor.lasta.data.model.user

enum class Language {
  ENGLISH,
  FRENCH,
  GERMAN;

  fun getEnglishName(): String {
    return name.lowercase().replaceFirstChar { it.uppercase() }
  }

  fun getFrenchName(): String {
    return when (this) {
      ENGLISH -> "Anglais"
      FRENCH -> "Français"
      GERMAN -> "Allemand"
    }
  }

  fun getGermanName(): String {
    return when (this) {
      ENGLISH -> "Englisch"
      FRENCH -> "Französisch"
      GERMAN -> "Deutsch"
    }
  }
}
