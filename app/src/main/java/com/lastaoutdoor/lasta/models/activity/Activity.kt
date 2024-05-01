package com.lastaoutdoor.lasta.models.activity

import com.lastaoutdoor.lasta.models.api.Position

data class Activity(
    val activityId: String,
    val osmId: Long,
    val activityType: ActivityType = ActivityType.CLIMBING,
    val name: String = "",
    val startPosition: Position = Position(0.0, 0.0),
    val rating: Float = 1.0f,
    val numRatings: Int = 0,
    val ratings: List<Rating> = emptyList(),
    val difficulty: Difficulty = Difficulty.EASY,
    val activityImageUrl: String = "",
    val climbingStyle: ClimbingStyle = ClimbingStyle.OUTDOOR,
    val elevationTotal: Float = 0f,
    val from: String = "",
    val to: String = "",
    val distance: Float = 0f
) {
  fun copy(activityId: String): Activity {
    return Activity(
        activityId,
        this.osmId,
        this.activityType,
        this.name,
        this.startPosition,
        this.rating,
        this.numRatings,
        this.ratings,
        this.difficulty,
        this.activityImageUrl,
        this.climbingStyle,
        this.elevationTotal,
        this.from,
        this.to,
        this.distance)
  }
}
