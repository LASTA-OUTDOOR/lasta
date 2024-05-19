package com.lastaoutdoor.lasta.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AppModuleTest {

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
  fun `One Tap client is provided`() {
    val signInClient = AppModule.provideOneTapClient(context)
    assertNotNull(signInClient)
  }

  @Test
  fun `Preferences repository is provided`() {
    val preferencesRepository = AppModule.providePreferencesRepository(context)
    assertNotNull(preferencesRepository)
  }

  @Test
  fun `Connectivity repository is provided`() {
    val connectivityRepository = AppModule.provideConnectivityRepository(context)
    assertNotNull(connectivityRepository)
  }

  @Test
  fun `Time provider is provided`() {
    val timeProvider = AppModule.provideTimeProvider()
    assertNotNull(timeProvider)
  }

  @Test
  fun `App database is provided`() {
    val appDatabase = AppModule.provideAppDatabase(context)
    assertNotNull(appDatabase)
  }

  @Test
  fun `Dao is provided`() {
    val dao = AppModule.provideDao(AppModule.provideAppDatabase(context))
    assertNotNull(dao)
  }

  @Test
  fun `Task repository is provided`() {
    val taskRepository =
        AppModule.provideTaskRepository(AppModule.provideDao(AppModule.provideAppDatabase(context)))
    assertNotNull(taskRepository)
  }

  @Test
  fun `Error toast is provided`() {
    val errorToast = AppModule.provideErrorToast(context)
    assertNotNull(errorToast)
  }

  @Test
  fun `Step counter is provided`() {
    val stepCounter = AppModule.provideStepCounter(context)
    assertNotNull(stepCounter)
  }
}
