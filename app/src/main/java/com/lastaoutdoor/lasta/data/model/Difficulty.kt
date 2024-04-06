package com.lastaoutdoor.lasta.data.model

enum class Difficulty {
  EASY,
  MODERATE,
  DIFFICULT;

  override fun toString(): String {
    return name.lowercase().replaceFirstChar { it.uppercase() }
  }
}
