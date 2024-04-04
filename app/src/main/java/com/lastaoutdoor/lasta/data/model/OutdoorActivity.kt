package com.lastaoutdoor.lasta.data.model

open class OutdoorActivity(
    private var activityType: ActivityType,
    /** The type of the activity. */

    /** Difficulty level of activity. */
    val difficulty: Int,

    /** The length of the activity. */
    val length: Float,

    /** The estimated duration of the activity. */
    val duration: String,

    /** The canton and postal code of the activity. */
    val locationName: String,

    /** The geographic position of the activity. */
) {

  public fun setActivityType(a: ActivityType) {
    activityType = a
  }

  public fun getActivityType(): ActivityType {
    return activityType
  }
}
