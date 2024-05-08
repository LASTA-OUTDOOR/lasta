package com.lastaoutdoor.lasta.ui.navigation

import androidx.navigation.NavHostController
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class NavigationStateTest {

  private lateinit var navigationState: NavigationState
  private val navController = mockk<NavHostController>(relaxed = true)

  @Before
  fun setUp() {
    navigationState = NavigationState(navController)
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun `Bottom bar tabs are correct`() {
    assert(navigationState.bottomBarTabs.size == 4)
    assert(navigationState.bottomBarTabs[0] == BottomBarTab.DISCOVER)
  }

  @Test
  fun `Current route is correct`() {
    every { navController.currentDestination?.route } returns "testRoute"
    assert(navigationState.currentRoute == "testRoute")
  }

  @Test
  fun `Navigate to route works`() {
    every { navController.currentDestination?.route } returns "testRoute"
    every { navController.popBackStack(any() as String, any(), any()) } returns true
    navigationState.navigateToBottomBarRoute("testRoute2")
    verify(exactly = 1) { navController.navigate(any() as String) }
  }

  @Test
  fun `Navigate to route doesn't work if passed same route`() {
    every { navController.currentDestination?.route } returns "testRoute"
    navigationState.navigateToBottomBarRoute("testRoute")
    verify(exactly = 0) { navController.navigate(any() as String) }
  }
}
