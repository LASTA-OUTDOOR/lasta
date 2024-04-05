package com.lastaoutdoor.lasta.data.model

import android.graphics.Bitmap
import java.util.Date

data class Trail(
    var avgSpeedInKMH: Float = 0f,
    var caloriesBurned: Int = 0,
    var difficulty: Difficulty? = null,
    var distanceInMeters: Int = 0,
    var elevationChangeInMeters: Int = 0,
    var img: Bitmap? = null,
    var popularity: Popularity? = null,
    var timeStarted: Long = 0L,
    var timeFinished: Long = 0L,
    var dayStarted: Date? = null,
)
