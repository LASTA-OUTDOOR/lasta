package com.lastaoutdoor.lasta.data.model.activity

enum class ClimbingStyle {
  OUTDOOR,
  INDOOR;

  override fun toString(): String {
    return name.lowercase().replaceFirstChar { it.uppercase() }
  }
}
