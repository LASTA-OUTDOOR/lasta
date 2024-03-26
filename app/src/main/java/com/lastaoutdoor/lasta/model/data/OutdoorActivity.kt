package com.lastaoutdoor.lasta.model.data

import java.util.Vector

data class OutdoorActivity(
    /** The type of the activity. */
    val type: String,

    /** Difficulty level of activity. */
    val difficulty: Int,

    /** The length of the activity. */
    val length: Float,

    /** The estimated duration of the activity. */
    val duration: String,

    /** The canton and postal code of the activity. */
    val locationName: String,

    /** The geographic position of the activity. */
    val exactLocation: Vector<Float>,
)