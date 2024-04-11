package com.lastaoutdoor.lasta.data.db

import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType

class ActivityConverter {
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

  private fun climbToDatabase(climb: ActivitiesDatabaseType.Climb): HashMap<String, Any> {
    return hashMapOf(
        "climbId" to climb.activityId, // Unique identifier for the climb activity
        "elevationGainedInMeters" to climb.elevationGainedInMeters, // Total elevation gained
        "numberOfPitches" to climb.numberOfPitches, // Number of pitches in a multi-pitch climb
        "timeStarted" to Timestamp(climb.timeStarted), // Start time of the climb
        "timeFinished" to Timestamp(climb.timeFinished), // End time of the climb
    )
  }

  private fun databaseToClimb(data: HashMap<String, Any>): ActivitiesDatabaseType.Climb {
    return ActivitiesDatabaseType.Climb(
        activityId = data["climbId"] as Long,
        elevationGainedInMeters = data["elevationGainedInMeters"] as Long,
        numberOfPitches = data["numberOfPitches"] as Long,
        timeStarted = (data["timeStarted"] as Timestamp).toDate(),
        timeFinished = (data["timeFinished"] as Timestamp).toDate())
  }

  fun activityToDatabase(activity: ActivitiesDatabaseType): HashMap<String, Any> {
    return when (activity.sport) {
      ActivitiesDatabaseType.Sports.HIKING ->
          trailToDatabase(activity as ActivitiesDatabaseType.Trail)
      ActivitiesDatabaseType.Sports.CLIMBING ->
          climbToDatabase(activity as ActivitiesDatabaseType.Climb)
    }
  }

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
