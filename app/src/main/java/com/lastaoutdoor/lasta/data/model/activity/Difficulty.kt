package com.lastaoutdoor.lasta.data.model.activity

enum class Difficulty {
  EASY,
  NORMAL,
  HARD;

  override fun toString(): String {
    return name.lowercase().replaceFirstChar { it.uppercase() }
  }
}
