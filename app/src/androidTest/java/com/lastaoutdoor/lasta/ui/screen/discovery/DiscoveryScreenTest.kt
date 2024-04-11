package com.lastaoutdoor.lasta.ui.screen.discovery

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule

@HiltAndroidTest
@UninstallModules(AppModule::class)
class DiscoveryScreenTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()
}
