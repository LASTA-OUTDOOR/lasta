package com.lastaoutdoor.lasta.viewmodel

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.lastaoutdoor.lasta.data.model.activity.Difficulty
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.profile.TimeFrame
import com.lastaoutdoor.lasta.di.TimeProvider
import com.lastaoutdoor.lasta.repository.ActivitiesRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import java.lang.reflect.Method
import java.time.LocalDate
import java.util.Date
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ProfileScreenViewModelTest {

  private lateinit var fakeActivitiesRepository: FakeActivitiesRepository
  private lateinit var profileScreenViewModel: ProfileScreenViewModel
  private lateinit var context: Context
  private lateinit var user: FirebaseUser
  private lateinit var mockTimeProvider: TimeProvider
  private lateinit var firebaseAuth: FirebaseAuth

  @Before
  fun setUp() {
    user = mockk<FirebaseUser>()
    context = ApplicationProvider.getApplicationContext()
    every { user.uid } returns "123"
    FirebaseApp.initializeApp(context)
    firebaseAuth = mockk<FirebaseAuth>()
    mockkStatic(FirebaseAuth::class)
    every { FirebaseAuth.getInstance() } returns firebaseAuth
    every { firebaseAuth.currentUser } returns user

    fakeActivitiesRepository = FakeActivitiesRepository()
    mockTimeProvider = mockk<TimeProvider>()
    every { mockTimeProvider.currentDate() } returns
        LocalDate.of(2022, 4, 15) // For example, April 15, 2022 Friday
    profileScreenViewModel = ProfileScreenViewModel(fakeActivitiesRepository, mockTimeProvider)
  }

  @Test
  fun `fetchUserActivities should update allTrailsCache and applyFilters`() = runTest {
    // Given
    val sport = ActivitiesDatabaseType.Sports.HIKING
    profileScreenViewModel.sport.value = sport

    // Then
    assertTrue(profileScreenViewModel.allTrails.value.isEmpty())
    assertTrue(profileScreenViewModel.trails.value.isEmpty())
    assertTrue(profileScreenViewModel.timeFrame.value == TimeFrame.W)
  }

  @Test
  fun `fetchUserActivitiesWithNonNullUser should getUserActivities and applyFilters`() = runTest {
    val sport = ActivitiesDatabaseType.Sports.HIKING
    profileScreenViewModel.sport.value = sport

    val activities =
        listOf(
            ActivitiesDatabaseType.Trail(
                1,
                Difficulty.EASY,
                null,
                null,
                Date(1649822400000),
                Date(1649822400000),
                5.0,
                100,
                1000,
                100),
            ActivitiesDatabaseType.Trail(
                2,
                Difficulty.MODERATE,
                null,
                null,
                Date(1649908800000),
                Date(1649908800000),
                4.0,
                200,
                2000,
                200),
            ActivitiesDatabaseType.Trail(
                3,
                Difficulty.DIFFICULT,
                null,
                null,
                Date(1649995200000),
                Date(1649995200000),
                3.0,
                300,
                3000,
                300))

    fakeActivitiesRepository.addActivityToUserActivities(user, activities[0])
    fakeActivitiesRepository.addActivityToUserActivities(user, activities[1])
    fakeActivitiesRepository.addActivityToUserActivities(user, activities[2])

    // When
    val method: Method = ProfileScreenViewModel::class.java.getDeclaredMethod("fetchUserActivities")
    method.isAccessible = true
    method.invoke(profileScreenViewModel)

    // Then
    assertTrue(profileScreenViewModel.allTrails.value.isNotEmpty())
    assertTrue(profileScreenViewModel.trails.value.isNotEmpty())
    assert(profileScreenViewModel.trails.value.size == 3)
    assert(profileScreenViewModel.trails.value[0].activityId == 1L)
    assert(profileScreenViewModel.trails.value[1].activityId == 2L)
    assert(profileScreenViewModel.trails.value[2].activityId == 3L)
    assert(profileScreenViewModel.trails.value[0].difficulty == Difficulty.EASY)
    assert(profileScreenViewModel.trails.value[1].difficulty == Difficulty.MODERATE)
    assert(profileScreenViewModel.trails.value[2].difficulty == Difficulty.DIFFICULT)
    assert(
        (profileScreenViewModel.trails.value[0] as ActivitiesDatabaseType.Trail).timeStarted ==
            Date(1649822400000))
    assert(
        (profileScreenViewModel.trails.value[1] as ActivitiesDatabaseType.Trail).timeStarted ==
            Date(1649908800000))
    assert(
        (profileScreenViewModel.trails.value[2] as ActivitiesDatabaseType.Trail).timeStarted ==
            Date(1649995200000))
    assert(
        (profileScreenViewModel.trails.value[0] as ActivitiesDatabaseType.Trail).timeFinished ==
            Date(1649822400000))
    assert(
        (profileScreenViewModel.trails.value[1] as ActivitiesDatabaseType.Trail).timeFinished ==
            Date(1649908800000))
    assert(
        (profileScreenViewModel.trails.value[2] as ActivitiesDatabaseType.Trail).timeFinished ==
            Date(1649995200000))
    assert(
        (profileScreenViewModel.trails.value[0] as ActivitiesDatabaseType.Trail).avgSpeedInKMH ==
            5.0)
    assert(
        (profileScreenViewModel.trails.value[1] as ActivitiesDatabaseType.Trail).avgSpeedInKMH ==
            4.0)
    assert(
        (profileScreenViewModel.trails.value[2] as ActivitiesDatabaseType.Trail).avgSpeedInKMH ==
            3.0)
    assert(
        (profileScreenViewModel.trails.value[0] as ActivitiesDatabaseType.Trail).caloriesBurned ==
            100L)
    assert(
        (profileScreenViewModel.trails.value[1] as ActivitiesDatabaseType.Trail).caloriesBurned ==
            200L)
    assert(
        (profileScreenViewModel.trails.value[2] as ActivitiesDatabaseType.Trail).caloriesBurned ==
            300L)
    assert(
        (profileScreenViewModel.trails.value[0] as ActivitiesDatabaseType.Trail).distanceInMeters ==
            1000L)
    assert(
        (profileScreenViewModel.trails.value[1] as ActivitiesDatabaseType.Trail).distanceInMeters ==
            2000L)
    assert(
        (profileScreenViewModel.trails.value[2] as ActivitiesDatabaseType.Trail).distanceInMeters ==
            3000L)
    assert(
        (profileScreenViewModel.trails.value[0] as ActivitiesDatabaseType.Trail)
            .elevationChangeInMeters == 100L)
    assert(
        (profileScreenViewModel.trails.value[1] as ActivitiesDatabaseType.Trail)
            .elevationChangeInMeters == 200L)
    assert(
        (profileScreenViewModel.trails.value[2] as ActivitiesDatabaseType.Trail)
            .elevationChangeInMeters == 300L)
  }

  @Test
  fun `test filterTrailsByTimeFrame with TimeFrame ALL`() = runTest {
    val trails =
        listOf(
            ActivitiesDatabaseType.Trail(
                1, Difficulty.EASY, null, null, Date(), Date(), 0.0, 0, 0, 0),
            ActivitiesDatabaseType.Trail(
                2, Difficulty.MODERATE, null, null, Date(), Date(), 0.0, 0, 0, 0))

    val method: Method =
        ProfileScreenViewModel::class
            .java
            .getDeclaredMethod("filterTrailsByTimeFrame", List::class.java, TimeFrame::class.java)
    method.isAccessible = true

    val result = method.invoke(profileScreenViewModel, trails, TimeFrame.ALL) as List<*>
    assertEquals(trails, result)
  }

  @Test
  fun `test trailStart before frame first and trailEnd before frame first`() {
    // Setup
    val trailStart = Date(1649568000000)
    val trailEnd = Date(1649568000000)
    val method: Method =
        ProfileScreenViewModel::class
            .java
            .getDeclaredMethod("filterTrailsByTimeFrame", List::class.java, TimeFrame::class.java)
    method.isAccessible = true

    val trail =
        ActivitiesDatabaseType.Trail(
            1, Difficulty.EASY, null, null, trailStart, trailEnd, 0.0, 0, 0, 0)
    val trails = listOf(trail)

    // Perform action
    val result = method.invoke(profileScreenViewModel, trails, TimeFrame.W) as List<*>

    // Verify result
    assertNotEquals(result, trails)
  }

  @Test
  fun `fetchUserActivities with null user should not proceed`() = runTest {
    // Given
    every { firebaseAuth.currentUser } returns null
    val sport = ActivitiesDatabaseType.Sports.HIKING
    profileScreenViewModel.sport.value = sport

    // When
    val method: Method = ProfileScreenViewModel::class.java.getDeclaredMethod("fetchUserActivities")
    method.isAccessible = true
    method.invoke(profileScreenViewModel)
    // Then
    assertTrue(profileScreenViewModel.allTrails.value.isEmpty())
    assertTrue(profileScreenViewModel.trails.value.isEmpty())

    every { firebaseAuth.currentUser } returns user
  }

  // Test trailStart after frame first and trailEnd before frame second
  @Test
  fun `test trailStart after frame first and trailEnd before frame second`() {
    // Setup
    val trailStart = Date(1649740800000) // After frameStart
    val trailEnd = Date(1649740800000) // Before frameEnd
    val method: Method =
        ProfileScreenViewModel::class
            .java
            .getDeclaredMethod("filterTrailsByTimeFrame", List::class.java, TimeFrame::class.java)
    method.isAccessible = true

    val trail =
        ActivitiesDatabaseType.Trail(
            1, Difficulty.EASY, null, null, trailStart, trailEnd, 0.0, 0, 0, 0)
    val trails = listOf(trail)

    // Perform action
    val result = method.invoke(profileScreenViewModel, trails, TimeFrame.W) as List<*>

    // Verify result
    assertTrue((result).isNotEmpty())
  }

  // Test trailStart before frame first and trailEnd after frame second
  @Test
  fun `test trailStart before frame first and trailEnd after frame second`() {
    // Setup
    val trailStart = Date(1649568000000) // Before frameStart
    val trailEnd = Date(1650076800000) // After frameEnd
    val method: Method =
        ProfileScreenViewModel::class
            .java
            .getDeclaredMethod("filterTrailsByTimeFrame", List::class.java, TimeFrame::class.java)
    method.isAccessible = true

    val trail =
        ActivitiesDatabaseType.Trail(
            1, Difficulty.EASY, null, null, trailStart, trailEnd, 0.0, 0, 0, 0)
    val trails = listOf(trail)

    // Perform action
    val result = method.invoke(profileScreenViewModel, trails, TimeFrame.W) as List<*>

    // Verify result
    assertTrue((result).isEmpty())
  }

  // Test trailStart after frame second and trailEnd after frame second
  @Test
  fun `test trailStart after frame second and trailEnd after frame second`() {
    // Setup
    val trailStart = Date(1650076800000) // After frameEnd
    val trailEnd = Date(1650076800000) // After frameEnd
    val method: Method =
        ProfileScreenViewModel::class
            .java
            .getDeclaredMethod("filterTrailsByTimeFrame", List::class.java, TimeFrame::class.java)
    method.isAccessible = true

    val trail =
        ActivitiesDatabaseType.Trail(
            1, Difficulty.EASY, null, null, trailStart, trailEnd, 0.0, 0, 0, 0)
    val trails = listOf(trail)

    // Perform action
    val result = method.invoke(profileScreenViewModel, trails, TimeFrame.W) as List<*>

    // Verify result
    assertTrue((result).isEmpty())
  }

  @Test
  fun `test setTimeFrame`() = runTest {
    val method: Method =
        ProfileScreenViewModel::class.java.getDeclaredMethod("setTimeFrame", TimeFrame::class.java)
    method.isAccessible = true

    val expectedTimeFrame = TimeFrame.W

    val collectJob = launch {
      profileScreenViewModel.timeFrame.collect { result -> assertEquals(expectedTimeFrame, result) }
    }

    method.invoke(profileScreenViewModel, expectedTimeFrame)

    delay(100) // Wait for a short time to allow the collection to complete

    collectJob.cancel() // Cancel the collection job
  }

  @Test
  fun `test setSport`() = runTest {
    val method: Method =
        ProfileScreenViewModel::class
            .java
            .getDeclaredMethod("setSport", ActivitiesDatabaseType.Sports::class.java)
    method.isAccessible = true

    val expectedSport = ActivitiesDatabaseType.Sports.HIKING

    val collectJob = launch {
      profileScreenViewModel.sport.collect { result -> assertEquals(expectedSport, result) }
    }

    method.invoke(profileScreenViewModel, expectedSport)

    delay(100) // Wait for a short time to allow the collection to complete

    collectJob.cancel() // Cancel the collection job
  }

  private class FakeActivitiesRepository : ActivitiesRepository {
    private val userActivitiesMap: MutableMap<String, MutableList<ActivitiesDatabaseType>> =
        mutableMapOf()

    override fun addActivityToUserActivities(user: FirebaseUser, activity: ActivitiesDatabaseType) {
      val userId = user.uid
      if (userActivitiesMap.containsKey(userId)) {
        userActivitiesMap[userId]?.add(activity)
      } else {
        userActivitiesMap[userId] = mutableListOf(activity)
      }
    }

    override suspend fun getUserActivities(
        user: FirebaseUser,
        activityType: ActivitiesDatabaseType.Sports
    ): List<ActivitiesDatabaseType> {
      val userId = user.uid
      return userActivitiesMap[userId] ?: emptyList()
    }
  }
}
