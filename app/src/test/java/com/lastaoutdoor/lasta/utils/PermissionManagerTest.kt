package com.lastaoutdoor.lasta.utils

import android.app.Activity
import androidx.test.core.app.ApplicationProvider
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PermissionManagerTest {

  private val activity = mockk<Activity>(relaxed = true)

  @Test
  fun `test requestNotificationPermission`() {
    requestNotificationPermission(ApplicationProvider.getApplicationContext(), activity)
  }
}
