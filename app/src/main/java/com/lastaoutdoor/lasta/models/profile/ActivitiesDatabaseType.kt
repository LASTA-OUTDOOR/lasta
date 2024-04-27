package com.lastaoutdoor.lasta.models.profile

import android.content.Context
import android.graphics.Bitmap
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ClimbingStyle
import com.lastaoutdoor.lasta.models.activity.Difficulty
import java.util.Date

/**
 * Sealed class representing different types of activities.
 *
 * @property sport The type of sport the activity is.
 * @property activityId The unique identifier of the activity.
 * @property timeStarted The start time of the activity.
 * @property timeFinished The end time of the activity.
 * @property popularity The popularity of the activity.
 * @property difficulty The difficulty level of the activity.
 * @property img The image associated with the activity.
 */
sealed class ActivitiesDatabaseType(
    val sport: Sports,
    open val activityId: Long = 0L,
    open val timeStarted: Date = Date(),
    open val timeFinished: Date = Date(),
    open val popularity: Popularity? = null,
    open val difficulty: Difficulty? = null,
    open val img: Bitmap? = null
) {

  /** Enum class representing different types of sports. */
  enum class Sports {
    HIKING,
    CLIMBING;

    override fun toString(): String {
      return name.lowercase().replaceFirstChar { it.uppercase() }
    }

    fun toStringCon(con: Context): String {
      return when (this) {
        HIKING -> con.getString(R.string.hiking)
        CLIMBING -> con.getString(R.string.climbing)
      }
    }
  }

  /**
   * Data class representing a Trail activity.
   *
   * @property activityId The unique identifier of the activity.
   * @property difficulty The difficulty level of the activity.
   * @property img The image associated with the activity.
   * @property popularity The popularity of the activity.
   * @property timeStarted The start time of the activity.
   * @property timeFinished The end time of the activity.
   * @property avgSpeedInKMH The average speed of the activity in km/h.
   * @property caloriesBurned The number of calories burned during the activity.
   * @property distanceInMeters The distance covered during the activity in meters.
   * @property elevationChangeInMeters The elevation change during the activity in meters.
   */
  data class Trail(
      override val activityId: Long = 0L,
      override val difficulty: Difficulty? = null,
      override val img: Bitmap? = null,
      override val popularity: Popularity? = null,
      override val timeStarted: Date = Date(),
      override val timeFinished: Date = Date(),
      val avgSpeedInKMH: Double = 0.0,
      val caloriesBurned: Long = 0,
      val distanceInMeters: Long = 0,
      val elevationChangeInMeters: Long = 0,
  ) : ActivitiesDatabaseType(Sports.HIKING)

  /**
   * Data class representing a Climb activity.
   *
   * @property activityId The unique identifier of the activity.
   * @property img The image associated with the activity.
   * @property popularity The popularity of the activity.
   * @property timeStarted The start time of the activity.
   * @property timeFinished The end time of the activity.
   * @property routeDifficulty The difficulty level of the route.
   * @property elevationGainedInMeters The elevation gained during the activity in meters.
   * @property numberOfPitches The number of pitches in the climb.
   * @property climbingStyle The style of climbing.
   */
  data class Climb(
      override val activityId: Long = 0L,
      override val img: Bitmap? = null,
      override val popularity: Popularity? = null,
      override val timeStarted: Date = Date(),
      override val timeFinished: Date = Date(),
      val routeDifficulty: Difficulty? = null,
      val elevationGainedInMeters: Long = 0,
      val numberOfPitches: Long = 0L,
      val climbingStyle: ClimbingStyle? = null,
  ) : ActivitiesDatabaseType(Sports.CLIMBING)
}
