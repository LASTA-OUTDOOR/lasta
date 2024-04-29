package com.lastaoutdoor.lasta.models.user

enum class Language {
  ENGLISH,
  FRENCH,
  GERMAN;

  override fun toString(): String {
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
