package com.lastaoutdoor.lasta.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NetworkModuleTest {

  private lateinit var context: Context

  @Before
  fun setUp() {
    context = ApplicationProvider.getApplicationContext()
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun `Begin sign in request is provided`() {
    val beginSignInRequest = NetworkModule.provideSignInRequest(context)
    assertNotNull(beginSignInRequest)
  }

  @Test
  fun `Begin sign up request is provided`() {
    val beginSignUpRequest = NetworkModule.provideSignUpRequest(context)
    assertNotNull(beginSignUpRequest)
  }

  @Test
  fun `OSM API service is provided`() {
    val osmApiService = NetworkModule.provideOSMAPIService(context)
    assertNotNull(osmApiService)
  }

  @Test
  fun `Weather API service is provided`() {
    val weatherApiService = NetworkModule.provideWeatherAPIService(context)
    assertNotNull(weatherApiService)
  }

  @Test
  fun `FCM API service is provided`() {
    val fcmApiService = NetworkModule.provideFcmAPIService(context)
    assertNotNull(fcmApiService)
  }

  @Test
  fun `Radar API service is provided`() {
    val radarApiService = NetworkModule.provideRadarApiService(context)
    assertNotNull(radarApiService)
  }

  @Test
  fun `Activities repository is provided`() {
    val activityRepository =
        NetworkModule.provideActivitiesRepository(NetworkModule.provideOSMAPIService(context))
    assertNotNull(activityRepository)
  }

  @Test
  fun `Weather repository is provided`() {
    val weatherRepository =
        NetworkModule.provideWeatherRepository(NetworkModule.provideWeatherAPIService(context))
    assertNotNull(weatherRepository)
  }

  @Test
  fun `Radar repository is provided`() {
    val radarRepository =
        NetworkModule.provideRadarRepository(NetworkModule.provideRadarApiService(context))
    assertNotNull(radarRepository)
  }

  @Test
  fun `Auth repository is provided`() {
    val authRepository =
        NetworkModule.provideAuthRepository(mockk(), mockk(), mockk(), mockk(), mockk())
    assertNotNull(authRepository)
  }

  @Test
  fun `User DB is provided`() {
    val database = mockk<FirebaseFirestore>()
    every { database.collection(any()) } returns mockk()
    val userDB = NetworkModule.provideUserDBRepository(context, database)
    assertNotNull(userDB)
  }

  @Test
  fun `Activities DB is provided`() {
    val database = mockk<FirebaseFirestore>()
    every { database.collection(any()) } returns mockk()
    val activitiesDB = NetworkModule.provideActivitiesDBRepository(context, database)
    assertNotNull(activitiesDB)
  }

  @Test
  fun `User Activities DB is provided`() {
    val database = mockk<FirebaseFirestore>()
    every { database.collection(any()) } returns mockk()
    val userActivitiesDB = NetworkModule.provideUserActivitiesDBRepository(context, database)
    assertNotNull(userActivitiesDB)
  }

  @Test
  fun `Social DB is provided`() {
    val database = mockk<FirebaseFirestore>()
    every { database.collection(any()) } returns mockk()
    val socialDB =
        NetworkModule.provideSocialDBRepository(context, database, mockk(), mockk(), mockk())
    assertNotNull(socialDB)
  }
}
