package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.MockTask
import com.lastaoutdoor.lasta.models.user.BikingUserActivity
import com.lastaoutdoor.lasta.models.user.ClimbingUserActivity
import com.lastaoutdoor.lasta.models.user.HikingUserActivity
import com.lastaoutdoor.lasta.models.user.UserActivity
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.util.ArrayList
import java.util.Date
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserActivitiesDBRepositoryImplTest {
  private lateinit var userActivitiesDB: UserActivitiesDBRepositoryImpl
  private val database: FirebaseFirestore = mockk(relaxed = true)
  private val context: Context = mockk()
  private val collection: CollectionReference = mockk(relaxed = true)
  private val documentReference: DocumentReference = mockk()
  private val documentSnapshot1: DocumentSnapshot = mockk()
  private val query: Query = mockk()
  private val querySnapshot: QuerySnapshot = mockk()
  private val addTask: MockTask<DocumentReference> = mockk()
  private val getTask: MockTask<DocumentSnapshot> = mockk()
  private val queryTask: MockTask<QuerySnapshot> = mockk()
  private val setTask: MockTask<Void> = mockk()

  @Before
  fun setUp() {
    every { context.getString(R.string.user_activities_db_name) } returns "user_activities"
    every { database.collection("user_activities") } returns collection

    every { collection.document(any()) } returns documentReference
    every { documentReference.get() } returns getTask

    every { collection.add(any()) } returns addTask

    every { collection.whereEqualTo(any() as String, any()) } returns query

    every { collection.whereIn(any() as FieldPath, any() as List<String>) } returns query
    every { collection.whereIn(any() as String, any() as List<String>) } returns query
    every { query.get() } returns queryTask

    every { addTask.isComplete } returns true
    every { addTask.isCanceled } returns false
    every { addTask.isSuccessful } returns true
    every { addTask.result } returns documentReference
    every { addTask.exception } returns null
    coEvery { addTask.await() } returns documentReference

    every { getTask.isComplete } returns true
    every { getTask.isCanceled } returns false
    every { getTask.isSuccessful } returns true
    every { getTask.result } returns documentSnapshot1
    every { getTask.exception } returns null
    coEvery { getTask.await() } returns documentSnapshot1

    every { queryTask.isComplete } returns true
    every { queryTask.isCanceled } returns false
    every { queryTask.isSuccessful } returns true
    every { queryTask.result } returns querySnapshot
    every { queryTask.exception } returns null
    coEvery { queryTask.await() } returns querySnapshot

    every { setTask.isComplete } returns true
    every { setTask.isCanceled } returns false
    every { setTask.isSuccessful } returns true
    every { setTask.result } returns mockk()
    every { setTask.exception } returns null
    coEvery { setTask.await() } returns mockk()

    every { documentSnapshot1.exists() } returns true
    every { documentSnapshot1.get("CLIMBING") } returns
        listOf(
            hashMapOf(
                "activityId" to "1",
                "timeStarted" to Timestamp(Date()),
                "timeFinished" to Timestamp(Date()),
                "numPitches" to 0L,
                "totalElevation" to 0.0))

    every { documentSnapshot1.get("HIKING") } returns
        listOf(
            hashMapOf(
                "activityId" to "1",
                "timeStarted" to Timestamp(Date()),
                "timeFinished" to Timestamp(Date()),
                "avgSpeed" to 0.0,
                "distanceDone" to 0.0,
                "elevationChange" to 0.0))

    every { documentSnapshot1.get("BIKING") } returns
        listOf(
            hashMapOf(
                "activityId" to "1",
                "timeStarted" to Timestamp(Date()),
                "timeFinished" to Timestamp(Date()),
                "avgSpeed" to 0.0,
                "distanceDone" to 0.0,
                "elevationChange" to 0.0))

    userActivitiesDB = UserActivitiesDBRepositoryImpl(context, database)
  }

  private fun assertUserActivitiesEqualWithoutDates(
      expected: List<UserActivity>,
      actual: List<UserActivity>
  ) {
    assertEquals(expected.size, actual.size)

    expected.zip(actual).forEach { (expectedActivity, actualActivity) ->
      assertEquals(expectedActivity.activityId, actualActivity.activityId)
      assertEquals(expectedActivity.activityType, actualActivity.activityType)
    }
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun `Get activity by id returns emptyList when activity does not exist`() = runTest {
    every { documentSnapshot1.get("CLIMBING") } returns listOf<UserActivity>()
    every { documentSnapshot1.get("HIKING") } returns listOf<UserActivity>()
    every { documentSnapshot1.get("BIKING") } returns listOf<UserActivity>()
    val result = userActivitiesDB.getUserActivities("randomid")
    assertEquals(result, ArrayList<UserActivity>())
  }

  @Test
  fun `Get activity by id returns correct output`() = runTest {
    val climbingUserActivity = ClimbingUserActivity("1")
    val hikingUserActivity = HikingUserActivity("1")
    val bikingUserActivity = BikingUserActivity("1")

    val result = userActivitiesDB.getUserActivities("randomid")
    val exp = ArrayList<UserActivity>()
    exp.add(climbingUserActivity)
    exp.add(hikingUserActivity)
    exp.add(bikingUserActivity)
    assertUserActivitiesEqualWithoutDates(exp, result)
  }

  @Test
  fun `add user activity works as intended`() = runTest {
    val climbingUserActivity = ClimbingUserActivity()
    every { documentReference.set(any(), any()) } returns setTask
    coEvery { setTask.await() } returns mockk()
    userActivitiesDB.addUserActivity("randomid", climbingUserActivity)
  }

  @Test
  fun `getNLatestActivities works as intended`() = runTest {
    val climbingUserActivity = ClimbingUserActivity("1", timeFinished = Date(1L))
    val hikingUserActivity = HikingUserActivity("1", timeFinished = Date(2L))
    val bikingUserActivity = BikingUserActivity("1", timeFinished = Date(3L))
    val bikingUserActivity2 = BikingUserActivity("1", timeFinished = Date(4L))
    every { documentSnapshot1.get("CLIMBING") } returns
        listOf(
            hashMapOf(
                "activityId" to "1",
                "timeStarted" to Timestamp(Date(1L)),
                "timeFinished" to Timestamp(Date(1L)),
                "numPitches" to 0L,
                "totalElevation" to 0.0))

    every { documentSnapshot1.get("HIKING") } returns
        listOf(
            hashMapOf(
                "activityId" to "1",
                "timeStarted" to Timestamp(Date(2L)),
                "timeFinished" to Timestamp(Date(2L)),
                "avgSpeed" to 0.0,
                "distanceDone" to 0.0,
                "elevationChange" to 0.0))

    every { documentSnapshot1.get("BIKING") } returns
        listOf(
            hashMapOf(
                "activityId" to "1",
                "timeStarted" to Timestamp(Date(3L)),
                "timeFinished" to Timestamp(Date(3L)),
                "avgSpeed" to 0.0,
                "distanceDone" to 0.0,
                "elevationChange" to 0.0),
            hashMapOf(
                "activityId" to "1",
                "timeStarted" to Timestamp(Date(4L)),
                "timeFinished" to Timestamp(Date(4L)),
                "avgSpeed" to 0.0,
                "distanceDone" to 0.0,
                "elevationChange" to 0.0))

    val result = userActivitiesDB.getNLatestActivities("randomid", 3)
    val exp = ArrayList<UserActivity>()
    exp.add(bikingUserActivity2)
    exp.add(bikingUserActivity)
    exp.add(hikingUserActivity)
    assertUserActivitiesEqualWithoutDates(exp, result)
  }

  @Test
  fun `getUserHikingActivities works as intended`() = runTest {
    val hikingUserActivity = HikingUserActivity("1")

    val result = userActivitiesDB.getUserHikingActivities("randomid")
    val exp = ArrayList<UserActivity>()
    exp.add(hikingUserActivity)
    assertUserActivitiesEqualWithoutDates(exp, result)
  }

  @Test
  fun `getUserClimbingActivities works as intended`() = runTest {
    val climbingUserActivity = ClimbingUserActivity("1")
    val result = userActivitiesDB.getUserClimbingActivities("randomid")
    val exp = ArrayList<UserActivity>()
    exp.add(climbingUserActivity)
    assertUserActivitiesEqualWithoutDates(exp, result)
  }

  @Test
  fun `getUserBikingActivities works as intended`() = runTest {
    val bikingUserActivity = BikingUserActivity("1")
    val result = userActivitiesDB.getUserBikingActivities("randomid")
    val exp = ArrayList<UserActivity>()
    exp.add(bikingUserActivity)
    assertUserActivitiesEqualWithoutDates(exp, result)
  }
}
