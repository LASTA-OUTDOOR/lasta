package com.lastaoutdoor.lasta.data.model.profile

import android.content.Context
import android.graphics.Bitmap
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.activity.ClimbingStyle
import com.lastaoutdoor.lasta.data.model.activity.Difficulty
import java.util.Date

sealed class ActivitiesDatabaseType(
    val sport: Sports,
    open val activityId: Long = 0L,
    open val timeStarted: Date = Date(),
    open val timeFinished: Date = Date(),
    open val popularity: Popularity? = null,
    open val difficulty: Difficulty? = null,
    open val img: Bitmap? = null
) {

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
