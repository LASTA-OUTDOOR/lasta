package com.lastaoutdoor.lasta.data.db

import android.content.Context
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
  private val updateTask: MockTask<Void> = mockk()

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

    every { updateTask.isComplete } returns true
    every { updateTask.isCanceled } returns false
    every { updateTask.isSuccessful } returns true
    every { updateTask.result } returns mockk()
    every { updateTask.exception } returns null
    coEvery { updateTask.await() } returns mockk()

    every { documentSnapshot1.exists() } returns true
    every { documentSnapshot1.get("Climbing") } returns listOf<UserActivity>()
    every { documentSnapshot1.get("Hiking") } returns listOf<UserActivity>()
    every { documentSnapshot1.get("Biking") } returns listOf<UserActivity>()

    userActivitiesDB = UserActivitiesDBRepositoryImpl(context, database)
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun `Get activity by id returns emptyList when activity does not exist`() = runTest {
    every { documentSnapshot1.get("Climbing") } returns listOf<UserActivity>()
    every { documentSnapshot1.get("Hiking") } returns listOf<UserActivity>()
    every { documentSnapshot1.get("Biking") } returns listOf<UserActivity>()
    val result = userActivitiesDB.getUserActivities("randomid")
    assertEquals(result, ArrayList<UserActivity>())
  }

  @Test
  fun `Get activity by id returns correct output`() = runTest {
    val climbingUserActivity = ClimbingUserActivity()
    val hikingUserActivity = HikingUserActivity()
    val bikingUserActivity = BikingUserActivity()
    every { documentSnapshot1.get("Climbing") } returns listOf<UserActivity>(climbingUserActivity)
    every { documentSnapshot1.get("Hiking") } returns listOf<UserActivity>(hikingUserActivity)
    every { documentSnapshot1.get("Biking") } returns listOf<UserActivity>(bikingUserActivity)
    val result = userActivitiesDB.getUserActivities("randomid")
    val exp = ArrayList<UserActivity>()
    exp.add(climbingUserActivity)
    exp.add(hikingUserActivity)
    exp.add(bikingUserActivity)
    assertEquals(exp, result)
  }

  @Test
  fun `add user activity works as intended`() = runTest {
    val climbingUserActivity = ClimbingUserActivity()
    every { documentReference.update("Climbing", any()) } returns updateTask
    coEvery { updateTask.await() } returns mockk()
    userActivitiesDB.addUserActivity("randomid", climbingUserActivity)
  }

  @Test
  fun `getNLatestActivities works as intended`() = runTest {
    val climbingUserActivity = ClimbingUserActivity(timeFinished = Date(1L))
    val hikingUserActivity = HikingUserActivity(timeFinished = Date(2L))
    val bikingUserActivity = BikingUserActivity(timeFinished = Date(3L))
    val bikingUserActivity2 = BikingUserActivity(timeFinished = Date(4L))
    every { documentSnapshot1.get("Climbing") } returns listOf<UserActivity>(climbingUserActivity)
    every { documentSnapshot1.get("Hiking") } returns listOf<UserActivity>(hikingUserActivity)
    every { documentSnapshot1.get("Biking") } returns
        listOf<UserActivity>(bikingUserActivity, bikingUserActivity2)
    val result = userActivitiesDB.getNLatestActivities("randomid", 3)
    val exp = ArrayList<UserActivity>()
    exp.add(bikingUserActivity2)
    exp.add(bikingUserActivity)
    exp.add(hikingUserActivity)
    assertEquals(exp, result)
  }

  @Test
  fun `getUserHikingActivities works as intended`() = runTest {
    val climbingUserActivity = ClimbingUserActivity()
    val hikingUserActivity = HikingUserActivity()
    val bikingUserActivity = BikingUserActivity()
    every { documentSnapshot1.get("Climbing") } returns listOf<UserActivity>(climbingUserActivity)
    every { documentSnapshot1.get("Hiking") } returns listOf<UserActivity>(hikingUserActivity)
    every { documentSnapshot1.get("Biking") } returns listOf<UserActivity>(bikingUserActivity)
    val result = userActivitiesDB.getUserHikingActivities("randomid")
    val exp = ArrayList<UserActivity>()
    exp.add(hikingUserActivity)
    assertEquals(exp, result)
  }

  @Test
  fun `getUserClimbingActivities works as intended`() = runTest {
    val climbingUserActivity = ClimbingUserActivity()
    val hikingUserActivity = HikingUserActivity()
    val bikingUserActivity = BikingUserActivity()
    every { documentSnapshot1.get("Climbing") } returns listOf<UserActivity>(climbingUserActivity)
    every { documentSnapshot1.get("Hiking") } returns listOf<UserActivity>(hikingUserActivity)
    every { documentSnapshot1.get("Biking") } returns listOf<UserActivity>(bikingUserActivity)
    val result = userActivitiesDB.getUserClimbingActivities("randomid")
    val exp = ArrayList<UserActivity>()
    exp.add(climbingUserActivity)
    assertEquals(exp, result)
  }

  @Test
  fun `getUserBikingActivities works as intended`() = runTest {
    val climbingUserActivity = ClimbingUserActivity()
    val hikingUserActivity = HikingUserActivity()
    val bikingUserActivity = BikingUserActivity()
    every { documentSnapshot1.get("Climbing") } returns listOf<UserActivity>(climbingUserActivity)
    every { documentSnapshot1.get("Hiking") } returns listOf<UserActivity>(hikingUserActivity)
    every { documentSnapshot1.get("Biking") } returns listOf<UserActivity>(bikingUserActivity)
    val result = userActivitiesDB.getUserBikingActivities("randomid")
    val exp = ArrayList<UserActivity>()
    exp.add(bikingUserActivity)
    assertEquals(exp, result)
  }
}
