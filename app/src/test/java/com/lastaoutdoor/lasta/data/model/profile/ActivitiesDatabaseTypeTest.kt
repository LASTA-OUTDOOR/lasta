package com.lastaoutdoor.lasta.data.model.profile

import android.content.Context
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test

class ActivitiesDatabaseTypeTest {
  private val context: Context = mockk()

  @Before
  fun setUp() {
    every { context.getString(R.string.climbing) } returns "Climbing"
    every { context.getString(R.string.hiking) } returns "Hiking"
    every { context.getString(R.string.biking) } returns "Biking"
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun activityType_resourcesToString() {
    assertEquals("Climbing", ActivityType.CLIMBING.resourcesToString(context))
    assertEquals("Hiking", ActivityType.HIKING.resourcesToString(context))
    assertEquals("Biking", ActivityType.BIKING.resourcesToString(context))
  }
}
