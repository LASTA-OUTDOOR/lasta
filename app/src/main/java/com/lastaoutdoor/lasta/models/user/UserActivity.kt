package com.lastaoutdoor.lasta.models.user

import com.lastaoutdoor.lasta.models.activity.ActivityType
import java.util.Date

sealed class UserActivity(
    val activityType: ActivityType,
    open val activityId: Long = 0L,
    open val timeStarted: Date = Date(),
    open val timeFinished: Date = Date()
)

data class ClimbingUserActivity(
    override val activityId: Long = 0L,
    override val timeStarted: Date = Date(),
    override val timeFinished: Date = Date(),
    val numPitches: Int = 0,
    val totalElevation: Float = 0f,
) : UserActivity(ActivityType.CLIMBING, activityId, timeStarted, timeFinished)

data class HikingUserActivity(
    override val activityId: Long = 0L,
    override val timeStarted: Date = Date(),
    override val timeFinished: Date = Date(),
    val avgSpeed: Float = 0f,
    val distanceDone: Float = 0f,
    val elevationChange: Float = 0f
) : UserActivity(ActivityType.HIKING, activityId, timeStarted, timeFinished)

data class BikinUserActivity(
    override val activityId: Long = 0L,
    override val timeStarted: Date = Date(),
    override val timeFinished: Date = Date(),
    val avgSpeed: Float = 0f,
    val distanceDone: Float = 0f,
    val elevationChange: Float = 0f
) : UserActivity(ActivityType.BIKING, activityId, timeStarted, timeFinished)
