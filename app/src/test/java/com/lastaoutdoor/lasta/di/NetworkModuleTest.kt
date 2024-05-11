package com.lastaoutdoor.lasta.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.lastaoutdoor.lasta.data.api.osm.ActivityRepositoryImpl
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NetworkModuleTest {

  private lateinit var context: Context

  @Before
  fun setUp() {
    context = ApplicationProvider.getApplicationContext()
  }

  //private val rep = ActivityRepositoryImpl(NetworkModule.provideOSMAPIService(con))
}