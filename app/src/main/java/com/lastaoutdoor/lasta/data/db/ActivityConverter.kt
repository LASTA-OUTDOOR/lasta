package com.lastaoutdoor.lasta.data.db

import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType

/** A converter class for converting between database and model representations of activities. */
class ActivityConverter {

  /**
   * Converts a Trail activity to a HashMap representation for database storage.
   *
   * @param trail The Trail activity to convert.
   * @return A HashMap representing the Trail activity.
   */
  private fun trailToDatabase(trail: ActivitiesDatabaseType.Trail): HashMap<String, Any> {
    return hashMapOf(
        "activityId" to trail.activityId,
        "averageSpeedInKMH" to trail.avgSpeedInKMH,
        "caloriesBurned" to trail.caloriesBurned,
        "distanceInMeters" to trail.distanceInMeters,
        "elevationChangeInMeters" to trail.elevationChangeInMeters,
        "timeStarted" to Timestamp(trail.timeStarted),
        "timeFinished" to Timestamp(trail.timeFinished),
    )
  }

  /**
   * Converts a HashMap representation of a Trail activity from the database to a Trail model.
   *
   * @param data The HashMap representing the Trail activity.
   * @return A Trail model.
   */
  private fun databaseToTrail(data: HashMap<String, Any>): ActivitiesDatabaseType.Trail {
    return ActivitiesDatabaseType.Trail(
        activityId = data["activityId"] as Long,
        avgSpeedInKMH = (data["averageSpeedInKMH"] as? Double) ?: 0.0,
        caloriesBurned = data["caloriesBurned"] as Long,
        distanceInMeters = data["distanceInMeters"] as Long,
        elevationChangeInMeters = data["elevationChangeInMeters"] as Long,
        timeStarted = (data["timeStarted"] as Timestamp).toDate(),
        timeFinished = (data["timeFinished"] as Timestamp).toDate())
  }

  /**
   * Converts a Climb activity to a HashMap representation for database storage.
   *
   * @param climb The Climb activity to convert.
   * @return A HashMap representing the Climb activity.
   */
  private fun climbToDatabase(climb: ActivitiesDatabaseType.Climb): HashMap<String, Any> {
    return hashMapOf(
        "climbId" to climb.activityId, // Unique identifier for the climb activity
        "elevationGainedInMeters" to climb.elevationGainedInMeters, // Total elevation gained
        "numberOfPitches" to climb.numberOfPitches, // Number of pitches in a multi-pitch climb
        "timeStarted" to Timestamp(climb.timeStarted), // Start time of the climb
        "timeFinished" to Timestamp(climb.timeFinished), // End time of the climb
    )
  }

  /**
   * Converts a HashMap representation of a Climb activity from the database to a Climb model.
   *
   * @param data The HashMap representing the Climb activity.
   * @return A Climb model.
   */
  private fun databaseToClimb(data: HashMap<String, Any>): ActivitiesDatabaseType.Climb {
    return ActivitiesDatabaseType.Climb(
        activityId = data["climbId"] as Long,
        elevationGainedInMeters = data["elevationGainedInMeters"] as Long,
        numberOfPitches = data["numberOfPitches"] as Long,
        timeStarted = (data["timeStarted"] as Timestamp).toDate(),
        timeFinished = (data["timeFinished"] as Timestamp).toDate())
  }

  /**
   * Converts an Activity to a HashMap representation for database storage.
   *
   * @param activity The Activity to convert.
   * @return A HashMap representing the Activity.
   */
  fun activityToDatabase(activity: ActivitiesDatabaseType): HashMap<String, Any> {
    return when (activity.sport) {
      ActivitiesDatabaseType.Sports.HIKING ->
          trailToDatabase(activity as ActivitiesDatabaseType.Trail)
      ActivitiesDatabaseType.Sports.CLIMBING ->
          climbToDatabase(activity as ActivitiesDatabaseType.Climb)
    }
  }

  /**
   * Converts a HashMap representation of an Activity from the database to an Activity model.
   *
   * @param data The HashMap representing the Activity.
   * @param activityType The type of the Activity (Hiking or Climbing).
   * @return An Activity model.
   */
  fun databaseToActivity(
      data: HashMap<String, Any>,
      activityType: ActivitiesDatabaseType.Sports
  ): ActivitiesDatabaseType {
    return when (activityType) {
      ActivitiesDatabaseType.Sports.HIKING -> databaseToTrail(data)
      ActivitiesDatabaseType.Sports.CLIMBING -> databaseToClimb(data)
    }
  }
}
