package com.lastaoutdoor.lasta.ui

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainActivityTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @Test
  fun testMainActivityIsLaunched() {

    launch(MainActivity::class.java).use {
      it.recreate()
      assertEquals(it.state, Lifecycle.State.RESUMED)
    }
  }
}
