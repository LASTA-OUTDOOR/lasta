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
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.ClimbingStyle
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.activity.Rating
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class ActivitiesDBRepositoryImplTest {

  private lateinit var activitiesDB: ActivitiesDBRepositoryImpl
  private val database: FirebaseFirestore = mockk(relaxed = true)
  private val context: Context = mockk()
  private val collection: CollectionReference = mockk(relaxed = true)
  private val documentReference: DocumentReference = mockk()
  private val documentSnapshot: DocumentSnapshot = mockk()
  private val query: Query = mockk()
  private val querySnapshot: QuerySnapshot = mockk()
  private val addTask: MockTask<DocumentReference> = mockk()
  private val getTask: MockTask<DocumentSnapshot> = mockk()
  private val queryTask: MockTask<QuerySnapshot> = mockk()
  private val updateTask: MockTask<Void> = mockk()

  @Before
  fun setUp() {
    every { context.getString(R.string.activities_db_name) } returns "activities"
    every { database.collection("activities") } returns collection

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
    every { getTask.result } returns documentSnapshot
    every { getTask.exception } returns null
    coEvery { getTask.await() } returns documentSnapshot

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

    activitiesDB = ActivitiesDBRepositoryImpl(context, database)
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun `Add existing returns false when activity already exists`() = runTest {
    every { querySnapshot.isEmpty } returns false
    val result = activitiesDB.addActivityIfNonExisting(Activity("", 0))
    assert(!result)
  }

  @Test
  fun `Add existing returns true when activity does not exist`() = runTest {
    every { querySnapshot.isEmpty } returns true
    val result =
        activitiesDB.addActivityIfNonExisting(
            Activity("", 0, ratings = listOf(Rating("userId", "comment", 5))))
    assert(result)
  }

  @Test
  fun `Get activity by id returns null when activity does not exist`() = runTest {
    every { documentSnapshot.exists() } returns false
    val result = activitiesDB.getActivityById("activityId")
    assert(result == null)
  }

  @Test
  fun `Get activity by id returns activity when activity exists`() = runTest {
    every { documentSnapshot.exists() } returns true
    every { documentSnapshot.get("startPosition") } returns hashMapOf("lat" to 0.0, "lon" to 0.0)
    every { documentSnapshot.id } returns "activityId"
    every { documentSnapshot.getLong("osmId") } returns 0
    every { documentSnapshot.getString("activityType") } returns "CLIMBING"
    every { documentSnapshot.getString("name") } returns "name"
    every { documentSnapshot.getDouble("rating") } returns 5.0
    every { documentSnapshot.getLong("numRatings") } returns 1
    every { documentSnapshot.get("ratings") } returns
        listOf(hashMapOf("userId" to "userId", "comment" to "comment", "rating" to 5))
    every { documentSnapshot.getString("difficulty") } returns "EASY"
    every { documentSnapshot.getString("activityImageUrl") } returns "activityImageUrl"
    every { documentSnapshot.getString("climbingStyle") } returns "OUTDOOR"
    every { documentSnapshot.getDouble("elevationTotal") } returns 100.0
    every { documentSnapshot.getString("from") } returns "from"
    every { documentSnapshot.getString("to") } returns "to"
    every { documentSnapshot.getDouble("distance") } returns 10.0
    val result = activitiesDB.getActivityById("activityId") as Activity
    assert(result.activityId == "activityId")
    assert(result.osmId == 0L)
    assert(result.activityType == ActivityType.CLIMBING)
    assert(result.name == "name")
    assert(result.startPosition.lat == 0.0)
    assert(result.startPosition.lon == 0.0)
    assert(result.rating == 5.0f)
    assert(result.numRatings == 1)
    assert(result.ratings.size == 1)
    assert(result.ratings[0].userId == "userId")
    assert(result.ratings[0].comment == "comment")
    assert(result.ratings[0].rating == 5)
    assert(result.difficulty == Difficulty.EASY)
    assert(result.activityImageUrl == "activityImageUrl")
    assert(result.climbingStyle == ClimbingStyle.OUTDOOR)
    assert(result.elevationTotal == 100.0f)
    assert(result.from == "from")
    assert(result.to == "to")
    assert(result.distance == 10.0f)
  }

  @Test
  fun `Get activity by id returns activity but every field is null`() = runTest {
    every { documentSnapshot.exists() } returns true
    every { documentSnapshot.getString(any()) } returns null
    every { documentSnapshot.getDouble(any()) } returns null
    every { documentSnapshot.getLong(any()) } returns null
    every { documentSnapshot.get(any() as String) } returns null
    every { documentSnapshot.id } returns "activityId"
    val result = activitiesDB.getActivityById("activityId") as Activity
    assert(result.activityId == "activityId")
    assert(result.osmId == 0L)
    assert(result.activityType == ActivityType.CLIMBING)
    assert(result.name == "")
    assert(result.startPosition.lat == 0.0)
    assert(result.startPosition.lon == 0.0)
    assert(result.rating == 5.0f)
    assert(result.numRatings == 1)
    assert(result.ratings.isEmpty())
    assert(result.difficulty == Difficulty.EASY)
    assert(result.activityImageUrl == "")
    assert(result.climbingStyle == ClimbingStyle.OUTDOOR)
    assert(result.elevationTotal == 0.0f)
    assert(result.from == "")
    assert(result.to == "")
    assert(result.distance == 0.0f)
  }

  @Test
  fun `Get activities by Ids returns empty list when no activities are found`() = runTest {
    val activityIds = listOf("")
    val result = activitiesDB.getActivitiesByIds(activityIds)
    assert(result.isEmpty())
  }

  @Test
  fun `Get activities by Ids returns list of activities when activities are found`() = runTest {
    every { querySnapshot.isEmpty } returns true
    var result = activitiesDB.getActivitiesByIds(listOf("activityId"))
    assert(result.isEmpty())
    every { querySnapshot.isEmpty } returns false
    every { querySnapshot.documents } returns listOf(documentSnapshot)
    every { documentSnapshot.exists() } returns true
    every { documentSnapshot.getString(any()) } returns null
    every { documentSnapshot.getDouble(any()) } returns null
    every { documentSnapshot.getLong(any()) } returns null
    every { documentSnapshot.get(any() as String) } returns null
    every { documentSnapshot.id } returns "activityId"
    result = activitiesDB.getActivitiesByIds(listOf("activityId"))
    assert(result.size == 1)
  }

  @Test
  fun `Get activities by OSM Ids returns empty list when no ids are passed`() = runTest {
    val ids = emptyList<Long>()
    val result = activitiesDB.getActivitiesByOSMIds(ids, false)
    assert(result.isEmpty())
  }

  @Test
  fun `Get activivites by OSM Ids returns empty list when nothing is found in DB`() = runTest {
    every { querySnapshot.isEmpty } returns true
    val result = activitiesDB.getActivitiesByOSMIds(listOf(0, 1, 2), false)
    assert(result.isEmpty())
  }
}
