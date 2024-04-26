package com.lastaoutdoor.lasta.data.model.activity

enum class ActivityType {
  CLIMBING,
  HIKING,
  BIKING;

  override fun toString(): String {
    return name.lowercase().replaceFirstChar { it.uppercase() }
  }
}
