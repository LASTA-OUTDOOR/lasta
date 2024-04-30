package com.lastaoutdoor.lasta.models.activity

enum class Difficulty {
  EASY,
  NORMAL,
  HARD;

  override fun toString(): String {
    return name.lowercase().replaceFirstChar { it.uppercase() }
  }
}
