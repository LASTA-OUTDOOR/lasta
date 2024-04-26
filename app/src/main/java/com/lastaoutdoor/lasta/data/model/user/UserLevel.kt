package com.lastaoutdoor.lasta.data.model.user

/** Enum class for hiking levels */
enum class UserLevel {
  BEGINNER,
  INTERMEDIATE,
  ADVANCED;

  override fun toString(): String {
    return name.lowercase().replaceFirstChar { it.uppercase() }
  }
}

data class UserActivitiesLevel(
    val climbingLevel: UserLevel,
    val hikingLevel: UserLevel,
    val bikingLevel: UserLevel
)
