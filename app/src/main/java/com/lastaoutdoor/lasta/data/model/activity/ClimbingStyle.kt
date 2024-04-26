package com.lastaoutdoor.lasta.data.model.activity

enum class ClimbingStyle {
  FREE_SOLO,
  OUTDOOR,
  INDOOR;

  override fun toString(): String {
    return name.lowercase().replaceFirstChar { it.uppercase() }
  }
}
