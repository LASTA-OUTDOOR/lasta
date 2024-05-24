package com.lastaoutdoor.lasta.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lastaoutdoor.lasta.models.activity.ActivityType
import java.util.Date

@Entity
sealed class UserActivity(
    var activityType: ActivityType,
    @PrimaryKey open val activityId: String = "",
    open val timeStarted: Date = Date(),
    open val timeFinished: Date = Date()
)

@Entity
data class ClimbingUserActivity(
    @PrimaryKey override val activityId: String = "",
    override val timeStarted: Date = Date(),
    override val timeFinished: Date = Date(),
    val numPitches: Int = 0,
    val totalElevation: Float = 0f,
) : UserActivity(ActivityType.CLIMBING, activityId, timeStarted, timeFinished)

@Entity
data class HikingUserActivity(
    @PrimaryKey override val activityId: String = "",
    override val timeStarted: Date = Date(),
    override val timeFinished: Date = Date(),
    val avgSpeed: Float = 0f,
    val distanceDone: Float = 0f,
    val elevationChange: Float = 0f
) : UserActivity(ActivityType.HIKING, activityId, timeStarted, timeFinished)

@Entity
data class BikingUserActivity(
    @PrimaryKey override val activityId: String = "",
    override val timeStarted: Date = Date(),
    override val timeFinished: Date = Date(),
    val avgSpeed: Float = 0f,
    val distanceDone: Float = 0f,
    val elevationChange: Float = 0f
) : UserActivity(ActivityType.BIKING, activityId, timeStarted, timeFinished)
