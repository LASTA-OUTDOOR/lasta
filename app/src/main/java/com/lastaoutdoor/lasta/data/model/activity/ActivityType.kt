package com.lastaoutdoor.lasta.data.model.activity

enum class ActivityType {
  NULL,
  CLIMBING {
    override fun toString(): String {
      return "climbing"
    }
  },
  HIKING {
    override fun toString(): String {
      return "hiking"
    }
  },
  BIKING {
    override fun toString(): String {
      return "biking"
    }
  }
}
