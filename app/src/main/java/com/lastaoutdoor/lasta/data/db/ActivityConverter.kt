package com.lastaoutdoor.lasta.data.db

import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.BikingUserActivity
import com.lastaoutdoor.lasta.models.user.ClimbingUserActivity
import com.lastaoutdoor.lasta.models.user.HikingUserActivity
import com.lastaoutdoor.lasta.models.user.UserActivity

class ActivityConverter {
  private fun databaseToHikes(data: List<HashMap<String, Any>>): List<HikingUserActivity> {
    return (data.map { databaseToHike(it) }).toList()
  }

  private fun databaseToHike(data: HashMap<String, Any>): HikingUserActivity {
    return HikingUserActivity(
        activityId = data["activityId"] as String,
        timeStarted = (data["timeStarted"] as Timestamp).toDate(),
        timeFinished = (data["timeFinished"] as Timestamp).toDate(),
        avgSpeed = (data["avgSpeed"] as Double).toFloat(),
        distanceDone = (data["distanceDone"] as Double).toFloat(),
        elevationChange = (data["elevationChange"] as Double).toFloat())
  }

  private fun databaseToTrails(data: List<HashMap<String, Any>>): List<BikingUserActivity> {
    return (data.map { databaseToTrail(it) }).toList()
  }

  private fun databaseToTrail(data: HashMap<String, Any>): BikingUserActivity {
    return BikingUserActivity(
        activityId = data["activityId"] as String,
        timeStarted = (data["timeStarted"] as Timestamp).toDate(),
        timeFinished = (data["timeFinished"] as Timestamp).toDate(),
        avgSpeed = (data["avgSpeed"] as Double).toFloat(),
        distanceDone = (data["distanceDone"] as Double).toFloat(),
        elevationChange = (data["elevationChange"] as Double).toFloat())
  }

  private fun databaseToClimbs(data: List<HashMap<String, Any>>): List<ClimbingUserActivity> {
    return (data.map { databaseToClimb(it) }).toList()
  }

  private fun databaseToClimb(data: HashMap<String, Any>): ClimbingUserActivity {
    return ClimbingUserActivity(
        activityId = data["activityId"] as String,
        timeStarted = (data["timeStarted"] as Timestamp).toDate(),
        timeFinished = (data["timeFinished"] as Timestamp).toDate(),
        numPitches = (data["numPitches"] as Long).toInt(),
        totalElevation = (data["totalElevation"] as Double).toFloat())
  }

    fun difficultyCycle(difficulty: String): String {
    return when (difficulty) {
      "EASY" -> "NORMAL"
      "NORMAL" -> "HARD"
      "HARD" -> "EASY"
      else -> "EASY"
    }
    }

  fun databaseToActivity(
      data: List<HashMap<String, Any>>,
      activityType: ActivityType
  ): List<UserActivity> {
    return when (activityType) {
      ActivityType.HIKING -> databaseToHikes(data)
      ActivityType.CLIMBING -> databaseToClimbs(data)
      ActivityType.BIKING -> databaseToTrails(data)
    }
  }
}
