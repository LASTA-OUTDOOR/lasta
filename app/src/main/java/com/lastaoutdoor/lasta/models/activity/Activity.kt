package com.lastaoutdoor.lasta.models.activity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lastaoutdoor.lasta.models.api.Position

@Entity
data class Activity(
    @PrimaryKey val activityId: String,
    val osmId: Long,
    val activityType: ActivityType = ActivityType.CLIMBING,
    val name: String = "",
    @Embedded val startPosition: Position = Position(0.0, 0.0),
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

  fun copy(startPosition: Position): Activity {
    return Activity(
        this.activityId,
        this.osmId,
        this.activityType,
        this.name,
        startPosition,
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

@Entity
class OActivity(
    @PrimaryKey val activityId: String,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "lat") val lat: Double = 0.0,
    @ColumnInfo(name = "lon") val lon: Double = 0.0,
    @ColumnInfo(name = "osmId") val osmId: Long = 0L,
    @ColumnInfo(name = "rating") val rating: Float = 1.0f,
    @ColumnInfo(name = "numRatings") val numRatings: Int = 0,
    @ColumnInfo(name = "difficulty") val difficulty: Difficulty = Difficulty.EASY,
    @ColumnInfo(name = "imageUrl") val activityImageUrl: String = "",
    @ColumnInfo(name = "climbingStyle") val climbingStyle: ClimbingStyle = ClimbingStyle.OUTDOOR,
    @ColumnInfo(name = "elevationTotal") val elevationTotal: Float = 0f,
    @ColumnInfo(name = "from") val from: String = "",
    @ColumnInfo(name = "to") val to: String = "",
    @ColumnInfo(name = "distance") val distance: Float = 0f
)
