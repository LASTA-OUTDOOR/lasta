package com.lastaoutdoor.lasta.data.db

import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.models.profile.ActivitiesDatabaseType
import java.util.Date
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class ActivityConverterTest {
  private lateinit var activityConverter: ActivityConverter
  private lateinit var fakeTrail: ActivitiesDatabaseType.Trail
  private lateinit var hashForTrail: HashMap<String, Any>
  private lateinit var fakeClimb: ActivitiesDatabaseType.Climb
  private lateinit var hashForClimb: HashMap<String, Any>

  @Before
  fun setUp() {
    activityConverter = ActivityConverter()
    fakeTrail =
        ActivitiesDatabaseType.Trail(
            activityId = 1,
            avgSpeedInKMH = 2.0,
            caloriesBurned = 3,
            distanceInMeters = 4,
            elevationChangeInMeters = 5,
            timeStarted = Date(6),
            timeFinished = Date(8))
    fakeClimb =
        ActivitiesDatabaseType.Climb(
            activityId = 0,
            elevationGainedInMeters = 1,
            numberOfPitches = 2,
            timeStarted = Date(3),
            timeFinished = Date(4))
    hashForTrail =
        hashMapOf(
            "activityId" to 1L,
            "averageSpeedInKMH" to 2.0,
            "caloriesBurned" to 3L,
            "distanceInMeters" to 4L,
            "elevationChangeInMeters" to 5L,
            "timeStarted" to Timestamp(0, 6000000),
            "timeFinished" to Timestamp(0, 8000000),
        )

    hashForClimb =
        hashMapOf(
            "climbId" to 0L, // Unique identifier for the climb activity
            "elevationGainedInMeters" to 1L, // Total elevation gained
            "numberOfPitches" to 2L, // Number of pitches in a multi-pitch climb
            "timeStarted" to Timestamp(0, 3000000), // Start time of the climb
            "timeFinished" to Timestamp(0, 4000000), // End time of the climb
        )
  }

  @Test
  fun `activityToDatabase works as intended with hiking`() {
    val input = activityConverter.activityToDatabase(fakeTrail)
    assertEquals(hashForTrail, input)
  }

  @Test
  fun `activityToDatabase works as intended with climbing`() {
    val input = activityConverter.activityToDatabase(fakeClimb)
    assertEquals(hashForClimb, input)
  }

  @Test
  fun `databaseToActivity works as intended with hiking`() {
    val input =
        activityConverter.databaseToActivity(hashForTrail, ActivitiesDatabaseType.Sports.HIKING)
    assertEquals(fakeTrail, input)
  }

  @Test
  fun `databaseToActivity works as intended with climbing`() {
    val input =
        activityConverter.databaseToActivity(hashForClimb, ActivitiesDatabaseType.Sports.CLIMBING)
    assertEquals(fakeClimb, input)
  }
}
