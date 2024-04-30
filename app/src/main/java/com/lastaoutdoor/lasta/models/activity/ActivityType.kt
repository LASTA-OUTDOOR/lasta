package com.lastaoutdoor.lasta.models.activity

enum class ActivityType {
  CLIMBING,
  HIKING,
  BIKING;

  override fun toString(): String {
    return name.lowercase().replaceFirstChar { it.uppercase() }
  }
}
