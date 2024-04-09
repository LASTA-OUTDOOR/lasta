package com.lastaoutdoor.lasta.data.model

import android.graphics.Bitmap
import java.util.Date

data class Trail(
    val activityID: Long = 0L,
    var avgSpeedInKMH: Double = 0.0,
    var caloriesBurned: Long = 0,
    var difficulty: Difficulty? = null,
    var distanceInMeters: Long = 0,
    var elevationChangeInMeters: Long = 0,
    var img: Bitmap? = null,
    var popularity: Popularity? = null,
    var timeStarted: Date = Date(),
    var timeFinished: Date = Date()
)