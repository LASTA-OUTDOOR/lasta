package com.lastaoutdoor.lasta.data.db

import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.data.model.Trail

class ActivityConverter() {
  fun trailToDatabase(trail: Trail): HashMap<String, Any> {
    return hashMapOf(
        "activityId" to trail.activityID,
        "averageSpeed" to trail.avgSpeedInKMH,
        "caloriesBurned" to trail.caloriesBurned,
        "distanceInMeters" to trail.distanceInMeters,
        "elevationChangeInMeters" to trail.elevationChangeInMeters,
        "timeStarted" to Timestamp(trail.timeStarted),
        "timeFinished" to Timestamp(trail.timeFinished),
    )
  }

  fun databaseToTrail(data: HashMap<String, Any>): Trail {
    return Trail(
        activityID = data["activityId"] as Long,
        avgSpeedInKMH = data["averageSpeed"] as Double,
        caloriesBurned = data["caloriesBurned"] as Long,
        distanceInMeters = data["distanceInMeters"] as Long,
        elevationChangeInMeters = data["elevationChangeInMeters"] as Long,
        timeStarted = (data["timeStarted"] as Timestamp).toDate(),
        timeFinished = (data["timeFinished"] as Timestamp).toDate())
  }
}
