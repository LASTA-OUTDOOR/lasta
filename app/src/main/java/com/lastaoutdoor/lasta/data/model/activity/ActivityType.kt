package com.lastaoutdoor.lasta.data.model.activity

enum class ActivityType {
  NULL,
  CLIMBING {
    override fun toString(): String {
      return "Climbing"
    }
  },
  HIKING {
    override fun toString(): String {
      return "Hiking"
    }
  },
  BIKING {
    override fun toString(): String {
      return "Biking"
    }
  }
}
